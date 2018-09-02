import React from 'react'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      showing: this.props.showing || false
    }
    this.onClickAction = this.onClickAction.bind(this)
  }

  onClickAction () {
    this.setState(oldstate => {
      oldstate.showing = !oldstate.showing
      return oldstate
    })
  }

  componentDidUpdate (oldProps, oldState) {
    if (oldProps.showing !== this.state.showing) (this.props.onToggle || function () {})(this.state.showing)
  }

  render () {
    const { showing } = this.state
    const { text, children } = this.props
    return (
      <div>
        <button className={this.props.klass || 'btn btn-primary bg-dark btn-lg btn-block text-left'}
          onClick={this.onClickAction}
        >{text}</button>
        <br />
        {showing ? children : <div />}
      </div>
    )
  }
}
