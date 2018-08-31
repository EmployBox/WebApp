import React from 'react'

import HttpRequest from './httpRequest'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = { wasClicked: false }
  }

  render () {
    const { method, url, authorization, body, buttonText, buttonClass, onLoad, onResult, onError } = this.props
    return this.state.wasClicked
      ? <HttpRequest method={method}
        url={url}
        authorization={authorization}
        body={body}
        onLoad={onLoad}
        onResult={onResult}
        onError={onError}
        afterResult={() => this.setState({wasClicked: false})} />
      : <button class={buttonClass} onClick={() => this.setState({wasClicked: true})} >{buttonText}</button>
  }
}
