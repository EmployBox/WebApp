import React from 'react'
import {Redirect, withRouter} from 'react-router-dom'
import URI from 'urijs'
import ReactTable from 'react-table'

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
    console.log(json)
    return (
      <div>
        <p>Select the curriculum to send</p>
        {json.size === 0
          ? <p>No Items</p>
          : <ReactTable
            data={json._embedded.items}
            columns={[
              {
                Header: 'Title',
                accessor: 'title'
              }
            ]} />}
      </div>
    )
  }

  render () {
    const {url, wasClicked, isLoggedIn, afterResult, onResult} = this.state
    const {auther, history, job} = this.props
    let modalBody
    if (wasClicked) {
      if (!isLoggedIn) modalBody = <Redirect to={auther.loginUrl + '?redirect=' + URI.encode(history.location.pathname)} />
      else if (url) modalBody = <HttpRequest url={url} authorization={auther.auth} onResult={onResult && this.onResult} afterResult={afterResult && this.afterResult} />
    }
    return (
      <div>
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
              </div>
              <div class='modal-footer'>
                <button type='button' class='btn btn-secondary' data-dismiss='modal' onClick={() => this.setState({wasClicked: false})}>Close</button>
                <button class='btn btn-success'>Send</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default withRouter(ApplyButton)