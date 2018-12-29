import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import GenericForm from '../components/forms/genericForm'
import URI from 'urijs'

const inputs = {
  user: [
    {
      type: 'number',
      name: 'competence',
      label: 'Competence'
    },
    {
      type: 'number',
      name: 'pontuality',
      label: 'Punctuality'
    },
    {
      type: 'number',
      name: 'assiduity',
      label: 'Assiduity'
    },
    {
      type: 'number',
      name: 'demeanor',
      label: 'Demeanor'
    }
  ],
  company: [
    {
      type: 'number',
      name: 'workLoad',
      label: 'Work Load'
    },
    {
      type: 'number',
      name: 'wage',
      label: 'Wage'
    },
    {
      type: 'number',
      name: 'workEnvironment',
      label: 'Work Environment'
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
    const {type, from, accountIdDest} = URI.parseQuery(this.props.location.search)
    const Form = ({method, res}) => <div class='container'>
      <GenericForm inputData={inputs[type].map(input => ({ ...input, value: res && res[input.name], max: 10, min: 0 }))}
        onSubmitHandler={inputs => {
          inputs.accountIdFrom = this.props.accountId
          inputs.accountIdTo = accountIdDest
          inputs.accountType = type === 'user' ? 'USR' : 'CMP'
          if (method === 'PUT') inputs.version = res.version
          this.setState({inputs: inputs})
        }}
      />
      {this.state.inputs
        ? <HttpRequest method={method} url={URI.decode(this.props.match.params.url)}
          authorization={this.props.auth}
          body={this.state.inputs}
          key={new Date().valueOf()}
          afterResult={json => this.props.history.push(from)}
        />
        : <div />}
    </div>
    return <div>
      <HttpRequest url={URI.decode(this.props.match.params.url) + '/single'}
        authorization={this.props.auth}
        onResult={res => <Form method='PUT' res={res} />}
        onError={() => <Form method='POST' />}
      />
    </div>
  }
})
