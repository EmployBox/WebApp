import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import URI from 'urijs'
import Table from '../../components/tables/halTable'
import URITemplate from 'urijs/src/URITemplate'

const jobTemplate = new URITemplate('/job/{url}')

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      deleteUrl: undefined,
      changeKey: new Date().valueOf()
    }
  }

  render () {
    const {match, auth, history, remove, template} = this.props
    const columns = [
      {
        Header: 'Job Info',
        columns: [
          {
            Header: 'Title',
            accessor: 'title'
          },
          {
            Header: 'Type',
            accessor: 'offerType'
          }
        ]
      }
    ]
    remove && columns.push({
      Header: 'Remove',
      Cell: (row) => <button class='fas fa-trash btn btn-light'
        type='button'
        aria-label='Close'
        onClick={() => this.setState({deleteUrl: row.original._links.self.href})} />
    })
    return <div>
      <HttpRequest url={URI.decode(match.params.offeredJobsUrl)} key={this.state.changeKey}
        authorization={auth}
        onResult={json =>
          <div class='container'>
            <Table json={json} currentUrl={URI.decode(match.params.offeredJobsUrl)}
              pushTo={url => history.push(template.expand({
                userUrl: URI.decode(match.params.url),
                offeredJobsUrl: url
              }))}
              onClickRow={({rowInfo, column}) => {
                if (!(column.Header === 'Name' || column.Header === 'Remove')) {
                  history.push(jobTemplate.expand({
                    url: rowInfo.original._links.self.href
                  }))
                }
              }}
              columns={columns}
            />
          </div>
        }
      />
      {this.state.deleteUrl && <HttpRequest method='DELETE' url={this.state.deleteUrl} authorization={auth} afterResult={json => {
        this.setState({deleteUrl: undefined, changeKey: new Date().valueOf()})
      }} />}
    </div>
  }
})
