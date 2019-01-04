import React from 'react'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      text: props.follows.size === 0 ? 'Follow' : 'Following',
      method: props.follows.size === 0 ? 'PUT' : 'DELETE',
      flag: false
    }
    this.changeState = this.changeState.bind(this)
    this.onClick = this.onClick.bind(this)
  }
  changeState (json) {
    this.setState(oldstate => {
      oldstate.text = oldstate.text === 'Follow' ? 'Folowing' : 'Follow'
      oldstate.method = oldstate.method === 'PUT' ? 'DELETE' : 'PUT'
      oldstate.flag = false
      return oldstate
    })
  }
  onClick () {
    this.setState(oldstate => {
      oldstate.flag = !oldstate.flag
      return oldstate
    })
  }
  render () {
    return (<div>
      <button class='btn btn-block btn-primary bg-dark' onClick={this.onClick} >{this.state.text}</button>
      {this.state.flag
        ? <HttpRequest method={this.state.method} url={this.props.url} authorization={auth}
          afterResult={this.changeState}
        />
        : <div />}
    </div>
    )
  }
}