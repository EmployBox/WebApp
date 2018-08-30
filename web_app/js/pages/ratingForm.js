import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import GenericForm from '../components/genericForm'
import URI from 'urijs'

const inputs = {
  user: [
    {
      type: 'number',
      name: 'competences'
    },
    {
      type: 'number',
      name: 'pontuality'
    },
    {
      type: 'number',
      name: 'assiduity'
    },
    {
      type: 'number',
      name: 'demeanor'
    }
  ],
  company: [
    {
      type: 'number',
      name: 'workLoad'
    },
    {
      type: 'number',
      name: 'wage'
    },
    {
      type: 'number',
      name: 'workEnvironment'
    }
  ]
}
export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      inputs: undefined
    }
  }
  render () {
    console.log(this.props.match)
    return this.state.inputs
      ? <HttpRequest url={URI.decode(this.props.match.url)} authorization={this.props.auth}
        afterResult={json => console.log('redirect to ' + JSON.stringify(json))}
      />
      : <GenericForm inputData={inputs[this.props.type]}
        onSumitHandler={inputs => this.setState({inputs: inputs})}
      />
  }
})