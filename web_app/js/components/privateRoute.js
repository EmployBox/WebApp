import React from 'react'
import {withRouter, Redirect, Route} from 'react-router-dom'
import URI from 'urijs'

export default (auther) => {
  return withRouter(({component: Component, history, ...rest}) => {
    return (
      <Route {...rest} render={(props) =>
        auther.auth
          ? <Component {...props} auth={auther.auth} />
          : <Redirect to={auther.loginUrl + '?redirect=' + URI.encode(history.location.pathname)} />} />
    )
  })
}
