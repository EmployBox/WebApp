import React from 'react'
import HttpRequest from './httpRequest'
import URI from 'urijs'
import GenericForm from './forms/genericForm'

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
    this.setState({inputs: inputs})
  }

  render () {
    return (
      <div>
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
  <div class='comment'>
    <HttpRequest url={}
      onResult={account => <h3 class='commentAuthor'>{account.name}</h3>}
    />
    
    {children}
  </div>
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
    return <div class='commentBox'>
      <h1>Comments</h1>
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
        : <div>No more comments</div>}
    </div>
  }
}
