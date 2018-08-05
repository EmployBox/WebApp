export default class {
  constructor (loginUrl) {
    this.auth = undefined
    this.loginUrl = loginUrl
  }

  authenticate (auth) {
    this.auth = auth
  }

  unAuthenticate () {
    this.auth = undefined
  }

  setLoginUrl (loginUrl) {
    this.loginUrl = loginUrl
  }
}
