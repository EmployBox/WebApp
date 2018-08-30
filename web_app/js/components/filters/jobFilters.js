import React from 'react'

import ContractForm from '../forms/contractForm'

export default (query, onQueryChange) => {
  const checkboxes = {}
  checkboxes['Looking for work'] = query.offerType === 'Looking for work' || query.offerType === undefined
  checkboxes['Looking for Worker'] = query.offerType === 'Looking for Worker' || query.offerType === undefined

  function handleCheckboxChange (checkbox) {
    const keys = Object.keys(checkboxes)
    const other = keys[0] === checkbox ? keys[1] : keys[0]

    let offerType
    if (checkboxes['Looking for work'] !== checkboxes['Looking for Worker']) offerType = checkboxes[checkbox] ? other : undefined
    else offerType = other

    if (offerType !== undefined) query.offerType = offerType
    else delete query.offerType

    onQueryChange(query)
  }

  function handleChange (value, property) {
    query[property] = value
    onQueryChange(query)
  }

  return (
    <div>
      <div class='form-group'>
        <label>Title</label>
        <input type='text' class='form-control' placeholder='Job Title' value={query.title || ''} onChange={(event) => handleChange(event.target.value, 'title')} />
      </div>
      <div class='form-group'>
        <label>Location</label>
        <input type='text' class='form-control' placeholder='Address' value={query.location || ''} onChange={(event) => handleChange(event.target.value, 'location')} />
      </div>
      <div class='form-group'>
        <label>Offer Type</label>
        <div class='form-check'>
          <input
            class='form-check-input'
            type='checkbox'
            checked={checkboxes['Looking for work']}
            onChange={() => handleCheckboxChange('Looking for work')} />
          <label class='form-check-label'>Looking for work</label>
        </div>
        <div class='form-check'>
          <input
            class='form-check-input'
            type='checkbox'
            checked={checkboxes['Looking for Worker']}
            onChange={() => handleCheckboxChange('Looking for Worker')} />
          <label class='form-check-label'>Looking for Worker</label>
        </div>
      </div>
      <div class='form-group'>
        <label>Account rating from</label>
        <div class='row'>
          <div class='col'>
            <input type='number' class='form-control' placeholder='#0' value={query.ratingLow || ''} onChange={(event) => handleChange(event.target.value, 'ratingLow')} />
          </div>
          <label>to</label>
          <div class='col'>
            <input type='number' class='form-control' placeholder='#10' value={query.ratingHigh || ''} onChange={(event) => handleChange(event.target.value, 'ratingHigh')} />
          </div>
        </div>
      </div>
      <div class='form-group'>
        <center>
          <button type='button' class='btn btn-secondary' data-toggle='modal' data-target='.bd-example-modal-lg'>Contract</button>
        </center>

        <div class='modal fade bd-example-modal-lg' tabIndex='-1' role='dialog' aria-labelledby='myLargeModalLabel' aria-hidden='true'>
          <div class='modal-dialog modal-lg modal-dialog-centered' role='document'>
            <div class='modal-content'>
              <div class='modal-body'>
                <ContractForm type={query.type}
                  schedules={query.schedules ? JSON.parse(query.schedules) : []}
                  onTypeChange={type => handleChange(type, 'type')}
                  onDatesChange={dates => handleChange(JSON.stringify(dates), 'schedules')} />
              </div>
              <div class='modal-footer'>
                <button type='button' class='btn btn-secondary' data-dismiss='modal'>Close</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
