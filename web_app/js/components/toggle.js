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
    return (
      <div>
        <button className='btn btn-primary bg-dark btn-lg btn-block' onClick={this.onClickAction}>{text}</button>
        <br />
        {showing ? children : <div />}
      </div>
    )
  }
}
