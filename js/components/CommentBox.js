import React from 'react'
import HttpRequest from './httpRequest'
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

const CommentList = ({data, loggedAccount, deleteComment, auth}) =>
  <div>
    {data.map(comment => (
      <Comment comment={comment} auth={auth} loggedAccount={loggedAccount} deleteComment={deleteComment} key={comment.commmentId}>
        {comment.text}
      </Comment>
    ))}
  </div>

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
      <div class='container'>
        {/* <GenericForm
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
        /> */}
        <textarea class='form-control' value={this.state.commentText} onChange={event => this.setState({ commentText: event.target.value })} placeholder='Insert your comment here' />
        <div class='row'>
          <div class='col-12'>
            <button
              type='button'
              onClick={() => this.onSubmit({text: this.state.commentText})}
              class='float-right btn btn-dark'>Submit</button>
          </div>
        </div>
        {this.state.inputs
          ? <HttpRequest method='POST' url={this.props.url.split('?')[0]} authorization={this.props.auth}
            body={this.state.inputs}
            afterResult={json => {
              this.setState({inputs: undefined})
              this.props.submitComment(json)
            }}
          />
          : <div />}
      </div>)
  }
}

const Comment = class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      deleteUrl: undefined
    }
  }

  render () {
    const {comment, children, loggedAccount, auth} = this.props
    return <div>
      <HttpRequest url={comment._links.account_from.href}
        authorization={auth}
        onResult={account => {
          const curr = new Date()
          const commentDate = new Date()
          commentDate.setTime(comment.datetime.epochSecond * 1000)
          return <div class='row'>
            <div class='col-sm-1'>
              <div class='thumbnail'>
                <HttpRequest url={account._links.self.href}
                  authorization={auth}
                  onResult={json =>
                    <img class='img-responsive img-thumbnail' src={(account.accountType === 'USR' ? json.photo_url : json.logo_url) || 'https://www.cukashmir.ac.in/facultyimages/2316218245609profile-default-male.png'} />}
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
            {loggedAccount === comment.accountIdFrom && <div>
              <button class='fas fa-trash btn btn-light'
                type='button'
                aria-label='Close'
                onClick={() => this.setState({deleteUrl: comment._links.self.href})} />
            </div>}
          </div>
        }}
      />
      {this.state.deleteUrl && <HttpRequest url={this.state.deleteUrl}
        method='DELETE'
        authorization={auth}
        afterResult={() => {
          this.props.deleteComment(this.state.deleteUrl)
          this.setState({deleteUrl: undefined})
        }} />}
    </div>
  }
}

export default class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      currentUrl: this.props.url,
      data: [],
      loadMore: true
    }
    this.deleteComment = this.deleteComment.bind(this)
    this.submitComment = this.submitComment.bind(this)
    this.loadNewEntries = this.loadNewEntries.bind(this)
  }

  deleteComment (link) {
    this.setState(oldstate => {
      oldstate.data = oldstate.data.filter(comment => comment._links.self.href !== link)
      return oldstate
    })
  }

  submitComment (json) {
    this.setState(oldstate => {
      oldstate.data.unshift(json)
      return oldstate
    })
  }

  loadNewEntries (json) {
    const {data} = this.state
    json._embedded && json._embedded.items ? this.setState({
      data: data.concat(json._embedded.items),
      currentUrl: json._links.next ? json._links.next.href : undefined,
      loadMore: false
    }) : this.setState({currentUrl: undefined, loadMore: false})
  }

  render () {
    return <div class='container'>
      <h3>Comments</h3>
      <CommentForm auth={this.props.auth} url={this.props.url} accountIdFrom={this.props.accountIdFrom} accountIdTo={this.props.accountIdTo} submitComment={this.submitComment} />
      <CommentList data={this.state.data} auth={this.props.auth} loggedAccount={this.props.loggedAccount} deleteComment={this.deleteComment} />
      {this.state.loadMore && <HttpRequest url={this.state.currentUrl}
        authorization={this.props.auth}
        afterResult={this.loadNewEntries}
        onResult={() => <div />}
      />}
      {this.state.currentUrl
        ? <button type='submit' onClick={() => {
          this.setState({
            loadMore: true
          })
        }}>More</button>
        : <div><br /> <div class='text-center'>No more comments</div></div>}
    </div>
  }
}
