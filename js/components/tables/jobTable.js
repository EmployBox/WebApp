import React from 'react'
import { withRouter } from 'react-router-dom'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import fetch from 'isomorphic-fetch'

import Table from './halTable'
import ApplyButton from '../buttons/applyButton'
import {checkAndParseResponse} from '../../utils/httpResponseHelper'

const searchTemplate = new URITemplate('/search/{url}')

class JobTable extends React.Component {
  constructor (props) {
    super(props)

    const { auther } = props

    this.state = {
      userSelf: auther.accountType === 'USR' ? auther.self : undefined,
      resumesUrl: undefined
    }
  }

  componentDidMount () {
    const { userSelf } = this.state
    const { auther } = this.props

    if (!userSelf) return

    fetch(userSelf, {
      method: 'GET',
      headers: {
        'Authorization': auther.auth
      }
    })
      .then(checkAndParseResponse)
      .then(user => this.setState({ resumesUrl: user._links.curricula.href }))
      .catch(this.errorHandler)
  }

  render () {
    const {json, match, history, jobTempl, auther} = this.props
    const {resumesUrl} = this.state

    return (
      <div class='row'>
        <div class='col'>
          <Table json={json} currentUrl={URI.decode(match.params.url)}
            pushTo={url => history.push(searchTemplate.expand({url: url}))}
            onClickRow={({rowInfo, column}) => {
              if (column.Header !== 'Name' && column.Header !== undefined) {
                history.push(jobTempl.expand({url: rowInfo.original._links.self.href}))
              }
            }}
            columns={[
              {
                Header: 'Job Info',
                columns: [
                  {
                    Header: 'Title',
                    accessor: 'title'
                  },
                  {
                    Header: 'Opens',
                    accessor: 'offerBeginDate'
                  },
                  {
                    Header: 'Location',
                    id: 'address',
                    accessor: i => i.address || 'Not defined'
                  },
                  {
                    Header: 'Type',
                    accessor: 'offerType'
                  },
                  {
                    Cell: ({original}) => {
                      return original.offerType === 'Looking for Worker' && <ApplyButton auther={auther} job={original} resumesUrl={resumesUrl} />
                    }
                  }
                ]
              }
            ]}
          />
        </div>
      </div>
    )
  }
}

export default withRouter(JobTable)
