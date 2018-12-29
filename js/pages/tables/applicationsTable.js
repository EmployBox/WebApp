import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import Table from '../../components/tables/halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/account/{userUrl}/applications/{applicationUrl}')
const curriculaTempl = new URITemplate('/curricula/{url}')
const jobTempl = new URITemplate('/job/{url}')

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      deleteUrl: undefined,
      changeKey: new Date().valueOf()
    }
  }
  render () {
    const {match, history, auth, remove} = this.props
    const columns = []
    remove && columns.push({
      Header: 'Remove',
      Cell: (row) => <button class='fas fa-trash btn btn-light'
        type='button'
        aria-label='Close'
        onClick={() => this.setState({deleteUrl: row.original._links.self.href})} />
    })
    return <div>
      <HttpRequest url={URI.decode(match.params.applicationsUrl)} key={this.state.changeKey}
        authorization={auth}
        onResult={json =>
          <div class='container'>
            <Table json={json} currentUrl={URI.decode(match.params.applicationsUrl)}
              pushTo={url => history.push(template.expand({
                userUrl: URI.decode(match.params.url),
                applicationUrl: url
              }))}
              onClickRow={({rowInfo, column}) => {
                console.log(column)
                if (column.parentColumn && column.parentColumn.Header === 'Job Info') {
                  history.push(jobTempl.expand({
                    url: rowInfo.original._embedded.job._links.self.href
                  }))
                } else {
                  history.push(curriculaTempl.expand({
                    url: rowInfo.original._embedded.curriculum._links.self.href
                  }))
                }
              }}
              columns={[
                {
                  Header: 'Job Info',
                  columns: [
                    {
                      Header: 'Title',
                      accessor: '_embedded.job.title'
                    },
                    {
                      Header: 'Address',
                      accessor: '_embedded.job.address'
                    },
                    {
                      Header: 'Wage',
                      accessor: '_embedded.job.wage'
                    },
                    {
                      Header: 'Type',
                      accessor: '_embedded.job.type'
                    }
                  ]
                },
                {
                  Header: 'Curriculum',
                  accessor: '_embedded.curriculum.title'
                }
              ]}
            />
          </div>}
      />
      {this.state.deleteUrl && <HttpRequest method='DELETE' url={this.state.deleteUrl} authorization={auth} afterResult={json => {
        this.setState({deleteUrl: undefined, changeKey: new Date().valueOf()})
      }} />}
    </div>
  }
})
