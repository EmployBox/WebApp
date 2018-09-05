import React from 'react'
import {withRouter, Route} from 'react-router-dom'
import HttpRequest from '../components/httpRequest'
import URI from 'urijs'
import URITemplate from 'urijs/src/URITemplate'
import AcademicTable from './tables/academicTable'
import ExperiencesTable from './tables/experiencesTable'
import PreviuosJobsTable from './tables/previuosJobsTable'
import ProjectsTable from './tables/projectsTable'
import TabRoute, { TabConfig } from '../components/tabRoute';

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
        <TabRoute auth={auth}
          tabConfigs={[
            new TabConfig(
              json._links.academicBackgrounds.href,
              'Academic Background',
              props => <AcademicTable auth={auth} {...props} />,
              academicTempl.expand({
                curriculaUrl: curriculaUrl,
                academicUrl: json._links.academicBackgrounds.href
              }),
              '/academic/:academicUrl'
            ),
            new TabConfig(
              json._links.experiences.href,
              'Experiences',
              props => <ExperiencesTable auth={auth} {...props} />,
              experiencesTempl.expand({
                curriculaUrl: curriculaUrl,
                experiencesUrl: json._links.experiences.href
              }),
              '/experiences/:experiencesUrl'
            ),
            new TabConfig(
              json._links.previousJobs.href,
              'Previous Jobs',
              props => <PreviuosJobsTable auth={auth} {...props} />,
              previuosJobsTempl.expand({
                curriculaUrl: curriculaUrl,
                previousJobsUrl: json._links.previousJobs.href
              }),
              '/previousJobs/:previuosJobsUrl'
            ),
            new TabConfig(
              json._links.projects.href,
              'Projects',
              props => <ProjectsTable auth={auth} {...props} />,
              projectsTempl.expand({
                curriculaUrl: curriculaUrl,
                projectsUrl: json._links.projects.href
              }),
              '/projects/:projectsUrl'
            )
          ]}
        />
      </div>
    }
  />
})
