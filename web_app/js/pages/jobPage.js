import React from 'react'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import Toggle from '../components/toggle'
import Table from '../components/tables/halTable'
import {withRouter, Link} from 'react-router-dom'

const userTempl = new URITemplate('/account/{url}')
const companyTempl = new URITemplate('/company/{url}')

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      experiences: undefined,
      showing: false
    }
  }

  render () {
    const {auth, match, history} = this.props
    return <HttpRequest url={URI.decode(match.params.url)}
      authorization={auth}
      onResult={jobRes =>
        <div class='text-center'>
          <h1>{jobRes.title}</h1>
          <h2>{jobRes.description}</h2>
          <h2>Wage:{jobRes.wage}</h2>
          <div class='d-flex flex-row justify-content-center'>
            <h2>From: </h2>
            <Link to={(jobRes._embedded.account.accountType === 'USR' ? userTempl : companyTempl)
              .expand({url: jobRes._embedded.account._links.self.href})}>
              <h2>
                {jobRes._embedded.account.name}
              </h2>
            </Link>
          </div>
          <h3>Rating: {jobRes._embedded.account.rating}</h3>
          <HttpRequest url={this.state.experiences || jobRes._links.experiences.href} authorization={auth}
            onResult={json =>
              <Toggle text='Experiences' showing={this.state.showing} onToggle={showing => this.setState({showing: showing})}>
                <Table currentUrl={this.state.experiences || jobRes._links.experiences.href} json={json}
                  pushTo={url => this.setState({experiences: url})}
                  columns={[
                    {
                      Header: 'Competence',
                      accessor: 'competence'
                    },
                    {
                      Header: 'Years',
                      accessor: 'years'
                    }
                  ]}
                />
              </Toggle>
            }
          />
        </div>
      }
    />
  }
})