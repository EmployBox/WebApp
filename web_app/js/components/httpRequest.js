import React from 'react'
import fetch from 'isomorphic-fetch'
import {makeCancellable} from './promises'

export default class extends React.Component {
  // ({[OPTIONAL] String: method, [OPTIONAL] String: authorization, String: url, [OPTIONAL] String: body, [OPTIONAL] Component: onLoad, [OPTIONAL] function: onError(error):Component,
  // [OPTIONAL] function: onResult(result):Component, [OPTIONAL] function: afterResult(json):void})
  constructor (props) {
    super(props)
    this.state = {
      loading: true,
      url: props.url,
      json: undefined,
      error: undefined,
      method: props.method,
      body: props.body
    }

    this.load = this.load.bind(this)
  }

  static getDerivedStateFromProps (nextProps, prevState) {
    if (nextProps.url === prevState.url &&
      JSON.stringify(nextProps.body) === JSON.stringify(prevState.body) &&
      nextProps.method === prevState.method) return null
    return {
      loading: true,
      url: nextProps.url,
      json: undefined,
      error: undefined,
      method: nextProps.method,
      body: undefined
    }
  }

  componentDidMount () {
    this.load(this.state.url, this.state.method, this.state.body)
  }

  componentDidUpdate (oldProps, oldState) {
    if (this.state.loading) this.load(this.state.url, this.state.method, this.state.body)
    else if (this.state.json !== undefined && this.props.afterResult) {
      this.props.afterResult(this.state.json)
    }
  }

  componentWillUnmount () {
    if (this.promise) {
      this.promise.cancel()
    }
  }

  load (url, method, body) {
    if (method === undefined) method = 'GET'
    if (body !== undefined) body = JSON.stringify(body)
    const obj = {
      method: method,
      headers: {
        'Authorization': this.props.authorization
        // 'Access-Control-Allow-Origin': 'http://localhost:8080'
      },
      body: body
    }

    if (body !== undefined) obj.headers['Content-Type'] = 'application/json'

    // console.log(`${method} - ${url}`)

    if (this.promise) {
      this.promise.cancel()
    }
    // console.log(obj)
    this.promise = makeCancellable(
      fetch(url, obj)
        .then(resp => {
          let ok = true
          if (resp.status >= 400) {
            ok = false
          }
          // text.json().then(json => [json, ok])
          return resp.text().then(text => text ? [JSON.parse(text), ok] : ['', ok])
        })
        .then(([json, ok]) => {
          if (!ok) throw new Error(JSON.stringify(json))

          this.setState({
            loading: false,
            json: json
          })
        })
        .catch(err => {
          this.setState({
            loading: false,
            error: err,
            json: undefined
          })
        })
    )
  }

  defaultOnError (err) {
    console.log(err)
    return <p>ERROR!</p>
  }

  defaultOnResult (data) {
    return <p>Success!</p>
  }

  render () {
    const { onLoad, onError, onResult } = this.props
    const { loading, error, json } = this.state

    if (loading) {
      return onLoad || <p>Loading...</p>
    } else if (error) {
      return onError ? onError(error) : this.defaultOnError(error)
    } else {
      return onResult ? onResult(json) : this.defaultOnResult(json)
    }
  }
}
