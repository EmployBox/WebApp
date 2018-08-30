import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import AcademicTable from './tables/academicTable'
import ExperiencesTable from './tables/experiencesTable'
import PreviuosJobsTable from './tables/previuosJobsTable'
import ProjectsTable from './tables/projectsTable'

const academicTempl = new URITemplate('/curricula/{curriculaUrl}/academic/{academicUrl}')
const experiencesTempl = new URITemplate('/curricula/{curriculaUrl}/experiences/{experiencesUrl}')
const previuosJobsTempl = new URITemplate('/curricula/{curriculaUrl}/previousJobs/{previousJobsUrl}')
const projectsTempl = new URITemplate('/curricula/{curriculaUrl}/projects/{projectsUrl}')

export default withRouter(({auth, history, match}) => {
  const CollectionButton = ({url, title, pushTo}) => (
    <HttpRequest url={url} authorization={auth}
      onResult={json => (
        <button type='button' onClick={() => history.push(pushTo)} class='btn btn-primary bg-dark'>
          <div>
            {json.size} {title}
          </div>
        </button>
      )}
    />
  )
  const curriculaUrl = URI.decode(match.params.url)
  return <HttpRequest url={curriculaUrl}
    authorization={auth}
    onResult={json =>
      <div class='container'>
        <h4 class='text-center'>Title: {json.title}</h4>
        <CollectionButton url={json._links.academicBackgrounds.href} title='Academic Background' pushTo={
          academicTempl.expand({
            curriculaUrl: curriculaUrl,
            academicUrl: json._links.academicBackgrounds.href
          })
        } />
        <CollectionButton url={json._links.experiences.href} title='Experiences' pushTo={
          experiencesTempl.expand({
            curriculaUrl: curriculaUrl,
            experiencesUrl: json._links.experiences.href
          })
        } />
        <CollectionButton url={json._links.previousJobs.href} title='Previous Jobs' pushTo={
          previuosJobsTempl.expand({
            curriculaUrl: curriculaUrl,
            previousJobsUrl: json._links.previousJobs.href
          })
        } />
        <CollectionButton url={json._links.projects.href} title='Projects' pushTo={
          projectsTempl.expand({
            curriculaUrl: curriculaUrl,
            projectsUrl: json._links.projects.href
          })
        } />
        <Route path={`${match.path}/academic/:academicUrl`} component={props =>
          <AcademicTable auth={auth} {...props} />
        } />
        <Route path={`${match.path}/experiences/:experiencesUrl`} component={props =>
          <ExperiencesTable auth={auth} {...props} />
        } />
        <Route path={`${match.path}/previousJobs/:previuosJobsUrl`} component={props =>
          <PreviuosJobsTable auth={auth} {...props} />
        } />
        <Route path={`${match.path}/projects/:projectsUrl`} component={props =>
          <ProjectsTable auth={auth} {...props} />
        } />
      </div>
    }
  />
})
