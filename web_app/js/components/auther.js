export default class {
  constructor (loginUrl) {
    this.auth = undefined
    this.loginUrl = loginUrl
  }

  authenticate (auth, json) {
    this.auth = auth
    this.accountId = json.accountId
    this.self = json._links.self.href
  }

  unAuthenticate () {
    this.auth = undefined
  }

  setLoginUrl (loginUrl) {
    this.loginUrl = loginUrl
  }
}
