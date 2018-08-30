import React from 'react'
import GenericForm from './genericForm'

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      editMode: false
    }
  }
  render () {
    const {text, inputData, onSubmitHandler} = this.props
    const {editMode} = this.state
    console.log(editMode)
    return this.state.editMode
      ? <div>
        <GenericForm inputData={inputData}
          onSubmitHandler={inputs => {
            onSubmitHandler(inputs)
            this.setState({editMode: false})
          }}
        />
      </div>
      : <div>
        {text}
        <button onClick={() => this.setState({editMode: true})}>Edit</button>
      </div>
  }
}
