import React from 'react'

export default class extends React.Component {
  constructor (props) {
    super(props)
  }

  render () {
    return (
      <div class='container'>
        <h1 align='center'>Create a job offer or show employers that you're available for a certain job</h1>
        <form>
          <div class='form-row'>
            <div class='form-group col-md-8'>
              <label>Title</label>
              <input type='email' class='form-control' placeholder='Mechanic Engineer' />
            </div>
            <div class='form-group col-md-2'>
              <label>Wage</label>
              <input type='number' class='form-control' placeholder='€' />
            </div>
            <div class='form-group col-md-2'>
              <label>Type</label>
              <select class='form-control' id='exampleFormControlSelect1'>
                <option>Job Offer</option>
                <option>Candidate</option>
              </select>
            </div>
          </div>
          <div class='form-group'>
            <label >Description</label>
            <textarea class='form-control' rows='3' />
          </div>
          <div class='form-group'>
            <label for='inputAddress'>Address</label>
            <input type='text' class='form-control' id='inputAddress' placeholder='1234 Main St' />
          </div>
          <div class='form-group'>
            <label for='inputAddress2'>Address 2</label>
            <input type='text' class='form-control' id='inputAddress2' placeholder='Apartment, studio, or floor' />
          </div>
          <div class='form-row'>
            <div class='form-group col-md-6'>
              <label for='inputCity'>City</label>
              <input type='text' class='form-control' id='inputCity' />
            </div>
            <div class='form-group col-md-4'>
              <label for='inputState'>State</label>
              <select id='inputState' class='form-control'>
                <option selected>Choose...</option>
                <option>...</option>
              </select>
            </div>
            <div class='form-group col-md-2'>
              <label for='inputZip'>Zip</label>
              <input type='text' class='form-control' id='inputZip' />
            </div>
          </div>
          <div class='form-group'>
            <div class='form-check'>
              <input class='form-check-input' type='checkbox' id='gridCheck' />
              <label class='form-check-label' for='gridCheck'>
                Check me out
              </label>
            </div>
          </div>
          <button type='submit' class='btn btn-success'>Create</button>
        </form>
      </div>
    )
  }
}
