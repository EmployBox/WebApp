import React, {Component} from 'react'

export default class extends Component {
  constructor (props) {
    super(props)
    this.state = {
      inputs: {}
    }
    this.onChangeHandler = this.onChangeHandler.bind(this)
  }

  render () {
    return (
      <form class='form-group'>
        {this.internalRender(this.props.inputData)}
        <button
          type='button'
          onClick={() => this.props.onSubmitHandler(this.state.inputs)}
          class='btn btn-primary btn-lg btn-block bg-dark'>Submit</button>
      </form>
    )
  }

  onChangeHandler (event) {
    const {value, name} = event.target
    this.setState(oldState => {
      oldState.inputs[name] = value
      return oldState
    })
  }

  internalRender (data) {
    return data.map(({type, name}, key) => (
      <div class={this.props.klass}>
        <input
          type={type}
          name={name}
          placeholder={name}
          onChange={this.onChangeHandler}
          key={key}
          class='form-control form-control-lg col'
        />
      </div>
    ))
  }
}
