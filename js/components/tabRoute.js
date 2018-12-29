import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from './httpRequest'
import {Tab, Tabs, TabList, TabPanel} from 'react-tabs'

export default withRouter(class extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      tabs: new Array(this.props.tabConfigs.length)
    }
    console.log('tabs ' + JSON.stringify(this.state.tabs))
  }

  shouldComponentUpdate (nextProps, nextState) {
    return !this.state.tabs.every(val => val != null)
  }

  render () {
    const {history, match, auth, tabConfigs} = this.props
    const CollectionButton = ({url, title, index}) => {
      return this.state.tabs[index] && this.state.tabs[index]._links.self.href === url ? `${this.state.tabs[index].size} ${title}`
        : <HttpRequest url={url} authorization={auth}
          onResult={json => `${json.size} ${title}`}
          afterResult={json => this.setState(oldstate => {
            oldstate.tabs[index] = json
            return oldstate
          })}
        />
    }
    return <Tabs onSelect={index => history.push(tabConfigs[index].redirect)}>
      <TabList>
        {tabConfigs.map((config, index) => <Tab key={index}>
          <CollectionButton url={config.url} title={config.title} index={index} />
        </Tab>)}
      </TabList>
      {tabConfigs.map((config, index) => <TabPanel key={index}>
        <Route path={`${match.path}${config.routePath}`} component={props => <config.render {...props} />} />
      </TabPanel>)}
    </Tabs>
  }
})

export class TabConfig {
  constructor (url, title, render, redirect, routePath) {
    this.url = url
    this.title = title
    this.render = render
    this.redirect = redirect
    this.routePath = routePath
  }
}
