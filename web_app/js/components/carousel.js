import React from 'react'

const styles = {
  schedule: {
    backgroundImage: 'url(\'https://sopro.io/wp-content/uploads/2018/04/schedule-prospecting-festive-1024x683.jpg\')'
  },
  officeImage: {
    backgroundImage: 'url(\'https://mir-s3-cdn-cf.behance.net/project_modules/fs/1246c248689535.59f673af8c81b.jpg\')'
  },
  evaluationImage: {
    backgroundImage: 'url(\'https://europeanwesternbalkans.com/wp-content/uploads/2015/08/evaluation.jpg\')'
  },
  textStyle: {
    color: 'white',
    textShadow: '-1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000, 1px 1px 0 #000'
  }
}

export default (props) => (
  <header>
    <div id='carouselExampleIndicators' class='carousel slide' data-ride='carousel'>
      <ol class='carousel-indicators'>
        <li data-target='#carouselExampleIndicators' data-slide-to='0' class='active' />
        <li data-target='#carouselExampleIndicators' data-slide-to='1' />
        <li data-target='#carouselExampleIndicators' data-slide-to='2' />
      </ol>
      <div class='carousel-inner' role='listbox'>
        {/* Slide One - Set the background image for this slide in the line below */}
        <div class='carousel-item active' style={styles.officeImage}>
          <div class='carousel-caption d-none d-md-block'>
            <h3 style={styles.textStyle}>Take the next step for a greater future</h3>
            <p style={styles.textStyle}>We will give you the best options for your next career or if you are starting your own company, we will search your next employees</p>
          </div>
        </div>
        {/* Slide Two - Set the background image for this slide in the line below  */}
        <div class='carousel-item' style={styles.evaluationImage}>
          <div class='carousel-caption d-none d-md-block'>
            <h3 style={styles.textStyle}>Reviews</h3>
            <p class={styles.textStyle}>Make reviews about the company you love or hate! And check the user's reviews before thinking of hiring.</p>
          </div>
        </div>
        {/* Slide Three - Set the background image for this slide in the line below */}
        <div class='carousel-item' style={styles.schedule}>
          <div class='carousel-caption d-none d-md-block'>
            <h3 style={styles.textStyle}>Work in a non regular time?</h3>
            <p style={styles.textStyle}>With us you can search for jobs or people who work with the same schedule as you!</p>
          </div>
        </div>
      </div>
      <a class='carousel-control-prev' href='#carouselExampleIndicators' role='button' data-slide='prev'>
        <span class='carousel-control-prev-icon' aria-hidden='true' />
        <span class='sr-only'>Previous</span>
      </a>
      <a class='carousel-control-next' href='#carouselExampleIndicators' role='button' data-slide='next'>
        <span class='carousel-control-next-icon' aria-hidden='true' />
        <span class='sr-only'>Next</span>
      </a>
    </div>
  </header>
)
