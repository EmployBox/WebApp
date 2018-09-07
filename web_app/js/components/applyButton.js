import React from 'react'
import {Redirect, withRouter} from 'react-router-dom'
import URI from 'urijs'

import HalTable from '../components/tables/halTable'
import HttpRequest from './httpRequest'

class ApplyButton extends React.Component {
  constructor (props) {
    super(props)
    let url
    let isLoggedIn = false
    if (props.auther.accountType === 'USR') url = props.auther.self
    if (props.auther.accountType) isLoggedIn = true

    this.state = {
      url: url,
      isLoggedIn: isLoggedIn,
      wasClicked: false,
      afterResult: true
    }

    this.afterResult = this.afterResult.bind(this)
    this.onResult = this.onResult.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (nextProps.auther.accountType) return null
    return {isLoggedIn: false}
  }

  afterResult (json) {
    this.setState({url: json._links.curricula.href, afterResult: false, onResult: true})
  }

  onResult (json) {
    return (
      <div>
        <p>Select the curriculum to send</p>
        <HalTable
          currentUrl={this.state.tableUrl || json._links.self.href}
          json={json}
          onClickRow={() => {}}
          pushTo={url => this.setState({tableUrl: url})}
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
                    disabled={this.state.selectedCV === original.curriculumId}
                    onClick={() => this.setState({selectedCV: original.curriculumId})}>Send this</button>
                )
              }
            }
          ]} />
      </div>
    )
  }

  render () {
    const {url, wasClicked, isLoggedIn, afterResult, onResult} = this.state
    const {auther, history, job} = this.props
    let modalBody
    if (wasClicked) {
      console.log(isLoggedIn)
      if (!isLoggedIn) modalBody = <Redirect to={auther.loginUrl + '?redirect=' + URI.encode(history.location.pathname)} />
      else if (url) modalBody = <HttpRequest url={url} authorization={auther.auth} onResult={onResult && this.onResult} afterResult={afterResult && this.afterResult} />
    }
    return (
      auther.accountType === 'USR'
        ? <div>
          <button type='button' class='btn btn-success' data-toggle={isLoggedIn && 'modal'} data-backdrop='static' data-target='#exampleModal' onClick={() => this.setState({wasClicked: true})}>
            Apply Now
          </button>

          <div class='modal fade' id='exampleModal' tabIndex='-1' role='dialog' aria-labelledby='exampleModalLabel' aria-hidden='true'>
            <div class='modal-dialog' role='document'>
              <div class='modal-content'>
                <div class='modal-header'>
                  <h5 class='modal-title' id='exampleModalLabel'>Apply to {job.title}</h5>
                  <button type='button' class='close' data-dismiss='modal' aria-label='Close' onClick={() => this.setState({wasClicked: false})}>
                    <span aria-hidden='true'>&times;</span>
                  </button>
                </div>
                <div class='modal-body'>
                  {modalBody}
                  {this.state.err && <div class='alert alert-danger' role='alert'>{this.state.err}</div>}
                </div>
                <div class='modal-footer'>
                  <button type='button' class='btn btn-secondary' data-dismiss='modal' onClick={() => this.setState({wasClicked: false})}>Close</button>
                  {this.state.body
                    ? <HttpRequest url={job._links.apply.href} authorization={auther.auth} method='POST' body={this.state.body} />
                    : <button
                      class='btn btn-success'
                      onClick={() => {
                        this.setState(prevState => {
                          return {body: {accountId: auther.accountId, jobId: 1, curriculumId: prevState.selectedCV}}
                        })
                      }}>Send</button>}
                </div>
              </div>
            </div>
          </div>
        </div>
        : this.state.body
          ? <HttpRequest
            url={job._links.apply.href}
            authorization={auther.auth}
            method='POST'
            body={this.state.body}
            onError={err => {
              console.log(err)
              setTimeout(() => this.setState({body: undefined}), 1000)
              return <p>ERROR!</p>
            }} />
          : <button
            class='btn btn-success'
            onClick={() => {
              this.setState(prevState => {
                return {body: {accountId: auther.accountId, jobId: 1, curriculumId: prevState.selectedCV}}
              })
            }}>Apply Now</button>
    )
  }
}

export default withRouter(ApplyButton)
