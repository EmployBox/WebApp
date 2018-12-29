import React from 'react'
import {withRouter} from 'react-router-dom'
import HttpRequest from '../../components/httpRequest'
import Table from '../../components/tables/halTable'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'

const template = new URITemplate('/account/{userUrl}/curriculas/{curriculaUrl}')
const curriculaTempl = new URITemplate('/curricula/{url}')

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
    const columns = [
      {
        Header: 'Title',
        accessor: 'title'
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
      <HttpRequest url={URI.decode(match.params.curriculaUrl)} key={this.state.changeKey}
        authorization={auth}
        onResult={json =>
          <div class='container'>
            <Table json={json} currentUrl={URI.decode(match.params.curriculaUrl)}
              pushTo={url => history.push(template.expand({
                userUrl: URI.decode(match.params.url),
                curriculaUrl: url
              }))}
              onClickRow={({rowInfo, column}) => {
                if (column.Header !== 'Remove') {
                  history.push(curriculaTempl.expand({
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
