import React from 'react'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import Toggle from '../components/toggle'
import Table from '../components/tables/halTable'
import {withRouter, Link} from 'react-router-dom'

const userTempl = new URITemplate('/account/{url}')
const companyTempl = new URITemplate('/company/{url}')
const curriculaTempl = new URITemplate('/curricula/{url}')

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      experiences: undefined,
      showingExp: false,
      applications: undefined,
      showingApp: false
    }
  }

  render () {
    const {auth, match, history, accountId} = this.props
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
          <h4>{jobRes.type}</h4>
          {jobRes.address && <h4>Address: {jobRes.address}</h4>}
          <HttpRequest url={this.state.experiences || jobRes._links.experiences.href} authorization={auth}
            onResult={json =>
              <Toggle text='Experiences' showing={this.state.showingExp} onToggle={showing => this.setState({showingExp: showing})}>
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
          {accountId === jobRes._embedded.account.accountId && <HttpRequest url={this.state.applications || jobRes._links.applications.href} authorization={auth}
            onResult={json =>
              <Toggle text='Applications' showing={this.state.showingApp} onToggle={showing => this.setState({showingApp: showing})}>
                <Table currentUrl={this.state.applications || jobRes._links.applications.href} json={json}
                  pushTo={url => this.setState({applications: url})}
                  onClickRow={({rowInfo, column}) => {
                    if (column.Header === 'Name') {
                      history.push((rowInfo.original._embedded.account.accountType === 'USR' ? userTempl : companyTempl).expand({
                        url: rowInfo.original._embedded.account._links.self.href
                      }))
                    } else {
                      history.push(curriculaTempl.expand({url: rowInfo.original._embedded.curriculum._links.self.href}))
                    }
                  }}
                  columns={[
                    {
                      Header: 'Name',
                      accessor: '_embedded.account.name'
                    },
                    {
                      Header: 'Curriculum',
                      accessor: '_embedded.curriculum.title'
                    }
                  ]}
                />
              </Toggle>
            }
          />}
        </div>
      }
    />
  }
})
