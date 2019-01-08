import React from 'react'
import {Redirect, withRouter} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import URI from 'urijs'

import HalTable from '../tables/halTable'
import {checkAndParseResponse} from '../../utils/httpResponseHelper'

class ApplyButton extends React.Component {
  constructor (props) {
    super(props)
    const { auther } = props

    this.state = {
      userSelf: auther.accountType === 'USR' ? auther.self : undefined,
      isLoggedIn: auther.accountType !== undefined,
      isLoading: false,
      resumesUrl: undefined,
      resumes: undefined,
      selectedResume: undefined,
      error: undefined
    }

    this.submitApplication = this.submitApplication.bind(this)
    this.errorHandler = this.errorHandler.bind(this)
    console.log(this.state.userSelf)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (nextProps.auther.accountType) return null
    return {isLoggedIn: false}
  }

  errorHandler (error) {
    console.log(`ApplyButton request error - ${error.message}`)
    this.setState({ isLoading: false, error: error })
  }

  fetchResumes () {
    const { isLoading, userSelf } = this.state
    const { auther } = this.props

    if (isLoading) return
    this.setState({ isLoading: true })

    fetch(userSelf, {
      method: 'GET',
      headers: {
        'Authorization': auther.auth
      }
    })
      .then(checkAndParseResponse)
      .then(user => fetchResumesFromUser(user))
      .catch(this.errorHandler)

    const fetchResumesFromUser = user => {
      let resumesUrl = user._links.curricula.href

      fetch(resumesUrl, {
        method: 'GET',
        headers: {
          'Authorization': auther.auth
        }
      })
        .then(checkAndParseResponse)
        .then(userResumes => this.setState({ isLoading: false, resumesUrl: resumesUrl, resumes: userResumes }))
        .catch(this.errorHandler)
    }
  }

  submitApplication () {
    const {selectedResume} = this.state
    const {auther, job} = this.props

    const body = {
      accountId: auther.accountId,
      jobId: job.jobId,
      curriculumId: selectedResume
    }

    fetch(job._links.apply.href, {
      method: 'POST',
      headers: {
        'Authorization': auther.auth,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    })
      .then(checkAndParseResponse)
      .catch(this.errorHandler)
  }

  render () {
    const {isLoggedIn, resumes, resumesUrl, isLoading, selectedResume, error} = this.state
    const {auther, history, job} = this.props

    return (
      <div>
        <button type='button' class='btn btn-success' data-toggle={isLoggedIn && 'modal'} data-backdrop='static' data-target={`#${job.jobId}`} onClick={() => { if (!resumes) this.fetchResumes() }} >
            Apply Now
        </button>

        <div class='modal fade' id={job.jobId} tabIndex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>
          <div class='modal-dialog' role='document'>
            <div class='modal-content'>
              <div class='modal-header'>
                <h5 class='modal-title' id='exampleModalLabel'>Apply to {job.title}</h5>
                <button type='button' class='close' data-dismiss='modal' aria-label='Close'>
                  <span aria-hidden='true'>&times;</span>
                </button>
              </div>
              <div class='modal-body'>
                {!isLoggedIn && <Redirect to={auther.loginUrl + '?redirect=' + URI.encode(history.location.pathname)} />}
                {isLoading ? (
                  <p>Loading...</p>
                ) : (
                  resumesUrl && resumes &&
                  <div>
                    <p>Select the curriculum to send</p>
                    <HalTable
                      currentUrl={resumesUrl}
                      json={resumes}
                      onClickRow={() => {}}
                      pushTo={url => this.setState({resumesUrl: url})}
                      columns={[
                        {
                          Header: 'Title',
                          accessor: 'title'
                        },
                        {
                          Cell: ({original}) => {
                            return (
                              <button
                                className='btn btn-primary'
                                disabled={selectedResume === original.curriculumId}
                                onClick={() => this.setState({selectedResume: original.curriculumId})}>
                                Send this
                              </button>
                            )
                          }
                        }
                      ]} />
                  </div>
                )}
                {error && <div class='alert alert-danger' role='alert'>{error.message}</div>}
              </div>
              <div class='modal-footer'>
                <button type='button' class='btn btn-secondary' data-dismiss='modal'>
                  Close
                </button>
                <button class='btn btn-success' onClick={this.submitApplication}>
                  Send
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(ApplyButton)
