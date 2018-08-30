import React from 'react'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      showing: false
    }
    this.onClickAction = this.onClickAction.bind(this)
  }

  onClickAction () {
    this.setState(oldstate => {
      oldstate.showing = !oldstate.showing
      return oldstate
    })
  }
  render () {
    const { showing } = this.state
    const { text, children } = this.props
    console.log(this.props.klass)
    return (
      <div>
        <button className={this.props.klass ? this.props.klass : 'btn btn-primary bg-dark btn-lg btn-block text-left'}
          onClick={this.onClickAction}
        >{text}</button>
        <br />
        {showing ? children : <div />}
      </div>
    )
  }
}
