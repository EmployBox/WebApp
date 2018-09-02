import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import GenericForm from '../components/forms/genericForm'
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
    const {type, from, accountIdDest} = URI.parseQuery(this.props.location.search)
    const Form = ({method, res}) => <div>
      <GenericForm inputData={inputs[type]}
        onSubmitHandler={inputs => {
          inputs.accountIdFrom = this.props.accountId
          inputs.accountIdDest = accountIdDest
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
