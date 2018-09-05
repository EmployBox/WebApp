import React, {Component} from 'react'

export default class extends Component {
  // { [{type, name, label, id}, ...]: inputData, string: klass, function: onSubmitHandler(inputs)}
  constructor (props) {
    super(props)
    this.state = {
      inputs: this.props.inputData.reduce((acc, curr) => { acc[curr.name] = curr.value; return acc }, {})
    }
    this.onChangeHandler = this.onChangeHandler.bind(this)
  }

  render () {
    return (
      <form class={this.props.formKlass || 'form-group'}
        onKeyPress={event => {
          const code = event.keyCode || event.which
          if (code === 13) this.buttonClick.click()
        }}>
        {this.internalRender(this.props.inputData)}
        <div class={this.props.klass}>
          {this.props.preBtn || <div />}
          <button
            type='button'
            ref={input => { this.buttonClick = input }}
            onClick={() => this.props.onSubmitHandler(this.state.inputs)}
            class={this.props.btnKlass || 'btn btn-primary btn-lg btn-block bg-dark'}>Submit</button>
        </div>
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
    return data.map(({label, id, value, name, klass, ...rest}, key) => (
      <div class={this.props.klass}>
        <label for={id}>{label}</label>
        <input
          class={klass || 'form-control form-control-lg col'}
          onChange={this.onChangeHandler}
          key={key}
          id={id}
          name={name}
          value={this.state.inputs[name]}
          {...rest}
        />
      </div>
    ))
  }
}
