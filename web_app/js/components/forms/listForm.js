import React from 'react'
import GenericForm from './genericForm'

const ListStyle = {
  maxHeight: '144px',
  overflowY: 'scroll'
}

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      inputList: []
    }
    this.addNewEntry = this.addNewEntry.bind(this)
  }
  addNewEntry (inputs) {
    const newInputList = this.state.inputList
    newInputList.push(Object.assign({}, inputs))
    this.setState({inputList: newInputList})
    this.props.submit(newInputList)
  }
  render () {
    console.log(this.state.inputList)
    return (
      <div>
        <h4 class='btn-primary bg-dark btn-lg btn-block'>{this.props.title}</h4>
        <div class='form-row'>
          <div class='form-group col-md-4'>
            <div class='card'>
              <ul class='list-group list-group-flush' style={ListStyle}>
                {this.state.inputList.map(value => <li class='list-group-item'>{this.props.row(value)}</li>)}
              </ul>
            </div>
          </div>
          <div class='form-group col-md-8'>
            <GenericForm
              inputData={this.props.inputData}
              formKlass='form-row'
              klass='form-group col'
              btnKlass='btn btn-success form-control'
              preBtn={<label class='text-white'>Ninja</label>}
              onSubmitHandler={this.addNewEntry}
            />
          </div>
        </div>
      </div>
    )
  }
}
