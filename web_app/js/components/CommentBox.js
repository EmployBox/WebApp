import React from 'react'
import HttpRequest from './httpRequest'
import URI from 'urijs'
import GenericForm from './forms/genericForm'

const DateDiff = {
  inMinutes: (d1, d2) => {
    const t2 = d2.getTime()
    const t1 = d1.getTime()

    return parseInt((t2 - t1) / (60 * 1000))
  },

  inHours: (d1, d2) => {
    const t2 = d2.getTime()
    const t1 = d1.getTime()

    return parseInt((t2 - t1) / (60 * 60 * 1000))
  },

  inDays: (d1, d2) => {
    const t2 = d2.getTime()
    const t1 = d1.getTime()

    return parseInt((t2 - t1) / (24 * 3600 * 1000))
  },

  inYears: (d1, d2) => {
    return d2.getFullYear() - d1.getFullYear()
  }
}

const CommentList = class extends React.Component {
  render () {
    return <div class='commentList'>
      {this.props.data.map(comment => (
        <Comment comment={comment} key={comment.commentId}>
          {comment.text}
        </Comment>
      ))}
    </div>
  }
}

const CommentForm = class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      inputs: undefined
    }
    this.onSubmit = this.onSubmit.bind(this)
  }

  onSubmit (inputs) {
    inputs.accountIdFrom = this.props.accountIdFrom
    inputs.accountIdTo = this.props.accountIdTo
    inputs.datetime = new Date().toISOString().replace(/([^T]+)T([^\\.]+).*/g, '$1 $2')
    this.setState({inputs: inputs})
  }

  render () {
    return (
      <div class='container text-center'>
        <GenericForm
          inputData={[
            {
              type: 'textbox',
              name: 'text',
              label: 'Insert here your comment',
              id: 'textId'
            }
          ]}
          klass='form-group'
          onSubmitHandler={this.onSubmit}
        />
        {this.state.inputs
          ? <HttpRequest method={'POST'} url={this.props.url.split('?')[0]} authorization={this.props.auth}
            body={this.state.inputs}
            afterResult={json => { console.log('comentario enviado ' + JSON.stringify(json))/* TODO */ }}
          />
          : <div />}
      </div>)
  }
}

const Comment = ({comment, children}) => (
  <HttpRequest url={comment._links.account_from.href}
    onResult={account => {
      const curr = new Date()
      const commentDate = new Date()
      commentDate.setTime(comment.datetime.epochSecond * 1000)
      console.log(account)
      return <div class='row'>
        <div class='col-sm-1'>
          <div class='thumbnail'>
            <HttpRequest url={account._links.self.href}
              onResult={json =>
                <img class='img-responsive img-thumbnail' src={account.accountType === 'USR' ? json.photo_url : json.logo_url || 'https://ssl.gstatic.com/accounts/ui/avatar_2x.png'} />}
            />
          </div>
        </div>
        <div class='col-sm-5'>
          <div class='panel panel-default'>
            <div class='panel-heading'>
              <strong>{account.name}</strong> <span class='text-muted'>commented {DateDiff.inDays(curr, commentDate)} days ago</span>
            </div>
            <div class='panel-body'>
              {children}
            </div>
          </div>
        </div>
      </div>
    }}
  />
)

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      currentUrl: this.props.url,
      data: [],
      page: 0
    }
  }
  render () {
    console.log(this.state.data)
    return <div class='container'>
      <h1 class='text-center'>Comments</h1>
      <CommentForm auth={this.props.auth} url={this.props.url} accountIdFrom={this.props.accountIdFrom} accountIdTo={this.props.accountIdTo} />
      <CommentList data={this.state.data} />
      {this.state.currentUrl
        ? (
          <div>
            <HttpRequest url={this.state.currentUrl} authorization={this.props.auth} key={this.state.currentUrl}
              afterResult={json => {
                const {data, currentUrl} = this.state
                this.setState({
                  data: (json._embedded ? json._embedded.items : []).concat(data),
                  page: json.current_page + 1,
                  currentUrl: json.current_page === json.last_page ? undefined : currentUrl
                })
              }
              } />
            <button onClick={() => {
              const {currentUrl, page} = this.state
              const uri = new URI(currentUrl)
              this.setState({
                currentUrl: uri.setQuery('page', page)
              })
            }}>More</button>
          </div>
        )
        : <div><br /> <div class='text-center'>No more comments</div></div>}
    </div>
  }
}
