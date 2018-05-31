import React from 'react'

import Navigation from '../components/navigation'
import Carousel from '../components/carousel'
import Footer from '../components/footer'

export default (props) => (
  <div>
    <Navigation />
    <Carousel />
    {/* Page Content */}
    <section class='py-5'>
      <div class='container'>
        <h1>Half Slider by Start Bootstrap</h1>
        <p>The background images for the slider are set directly in the HTML using inline CSS. The rest of the styles for this
            template are contained within the <code>half-slider.css</code> file.
        </p>
      </div>
    </section>
    <Footer />
  </div>
)
