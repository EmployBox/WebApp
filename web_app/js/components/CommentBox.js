import React from 'react'
import HttpRequest from './httpRequest'
import URI from 'urijs'
import GenericForm from './genericForm'

const CommentList = class extends React.Component {
  render () {
    return <div class='commentList'>
      {this.props.data.map(comment => (
        <Comment author={comment.author} key={comment.id}>
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
  }

  onSubmit (inputs) {
    this.setState({inputs: inputs})
  }

  render () {
    return (
      <div>
        <GenericForm
          inputData={[
            {
              type: 'textbox',
              name: 'Comment',
              label: 'Insert here your comment',
              id: 'commentId'
            }
          ]}
          klass='form-group'
          onSubmitHandler={this.onSubmit}
        />
        {this.state.inputs
          ? <HttpRequest method={'POST'} url={this.props.url.split('?')[0]}
            afterResult={json => { /* TODO */ }}
          />
          : <div />}
      </div>)
  }
}

const Comment = ({author, children}) => (
  <div class='comment'>
    <h2 class='commentAuthor'>{author}</h2>
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
      <CommentForm />
      <CommentList data={this.state.data} />
      {this.state.currentUrl
        ? (
          <div>
            <HttpRequest url={this.state.currentUrl} authorization={this.props.auth} key={this.state.currentUrl}
              afterResult={json => {
                const {data, currentUrl} = this.state
                this.setState({
                  data: json._embedded ? data.push(json._embedded.items) : data,
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

