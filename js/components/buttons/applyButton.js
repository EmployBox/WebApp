import React from 'react'
import {Redirect, withRouter} from 'react-router-dom'
import fetch from 'isomorphic-fetch'
import URI from 'urijs'

// import HalTable from '../tables/halTable'
import ReactTable from 'react-table'
import {checkAndParseResponse} from '../../utils/httpResponseHelper'

class ApplyButton extends React.Component {
  constructor (props) {
    super(props)
    const { auther, resumesUrl } = props

    this.state = {
      userSelf: auther.accountType === 'USR' ? auther.self : undefined,
      isLoggedIn: auther.accountType !== undefined,
      isLoading: false,
      resumesUrl: resumesUrl,
      resumes: undefined,
      selectedResume: undefined,
      error: undefined
    }

    this.submitApplication = this.submitApplication.bind(this)
    this.errorHandler = this.errorHandler.bind(this)
    this.fetchResumes = this.fetchResumes.bind(this)
    console.log(this.state.userSelf)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (nextProps.auther.accountType && nextProps.resumesUrl === prevState.resumesUrl) return null
    return {
      isLoggedIn: nextProps.auther.accountType !== undefined,
      resumesUrl: nextProps.resumesUrl
    }
  }

  errorHandler (error) {
    console.log(`ApplyButton request error - ${error.message}`)
    this.setState({ isLoading: false, error: error })
  }

  fetchResumes (state) {
    const { isLoading, resumesUrl } = this.state
    const { auther } = this.props

    if (isLoading || !resumesUrl) return
    this.setState({ isLoading: true })

    console.log(state)
    const uri = new URI(resumesUrl)
    if (state) {
      const {id, desc} = state.sorted
      uri.setQuery('page', state.page)
        .setQuery('pageSize', state.pageSize)
        .setQuery('orderColumn', id).setQuery('orderClause', desc ? 'DESC' : 'ASC')
    }

    fetch(uri.href(), {
      method: 'GET',
      headers: {
        'Authorization': auther.auth
      }
    })
      .then(checkAndParseResponse)
      .then(userResumes => this.setState({
        isLoading: false,
        resumes: userResumes
      }))
      .catch(this.errorHandler)
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
    const {isLoggedIn, resumes, isLoading, resumesUrl, selectedResume, error} = this.state
    const {auther, history, job} = this.props

    let pageSize = 10
    if (resumesUrl) {
      const uri = new URI(resumesUrl)
      const query = URI.parseQuery(uri.query())
      pageSize = query.pageSize
    }

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
                <div>
                  <p>Select the curriculum to send</p>
                  <ReactTable
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
                    ]}
                    manual
                    data={resumes ? resumes._embedded.items : []}
                    pages={resumes ? resumes.last_page + 1 : 0}
                    pageSize={Number(pageSize)}
                    loading={isLoading}
                    onFetchData={this.fetchResumes}
                  />
                </div>
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
