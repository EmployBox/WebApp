import React from 'react'
import {withRouter, Redirect, Route} from 'react-router-dom'
import URI from 'urijs'
// TODO make auth hole auth object

export default (auther) => withRouter(({component: Component, history, ...rest}) =>
  <Route {...rest} render={(props) =>
    auther.auth
      ? <Component {...props} auth={auther.auth} accountId={auther.accountId} />
      : <Redirect to={auther.loginUrl + '?redirect=' + URI.encode(history.location.pathname)} />} />
)
