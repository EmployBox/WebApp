import React from 'react'

export default (query, onQueryChange) => {
  const checkboxes = {
    work: query.type === 'work' || query.type === undefined,
    worker: query.type === 'worker' || query.type === undefined
  }

  function handleCheckboxChange (checkbox) {
    const keys = Object.keys(checkboxes)
    const other = keys[0] === checkbox ? keys[1] : keys[0]

    let type
    if (checkboxes.work !== checkboxes.worker) type = checkboxes[checkbox] ? other : undefined
    else type = other

    if (type !== undefined) query.type = type
    else delete query.type

    onQueryChange(query)
  }

  function handleChange (event, property) {
    query[property] = event.target.value
    onQueryChange(query)
  }

  return (
    <form>
      <div class='form-group'>
        <label>Title</label>
        <input type='text' class='form-control' placeholder='Job Title' value={query.title || ''} onChange={(event) => handleChange(event, 'title')} />
      </div>
      <div class='form-group'>
        <label>Location</label>
        <input type='text' class='form-control' placeholder='Address' value={query.location || ''} onChange={(event) => handleChange(event, 'location')} />
      </div>
      <div class='form-group'>
        <label>Type</label>
        <div class='form-check'>
          <input
            class='form-check-input'
            type='checkbox'
            checked={checkboxes.work}
            onChange={() => handleCheckboxChange('work')} />
          <label class='form-check-label'>Looking for work</label>
        </div>
        <div class='form-check'>
          <input
            class='form-check-input'
            type='checkbox'
            checked={checkboxes.worker}
            onChange={() => handleCheckboxChange('worker')} />
          <label class='form-check-label'>Looking for Worker</label>
        </div>
      </div>
      <div class='form-group'>
        <label>Account rating from</label>
        <div class='row'>
          <div class='col'>
            <input type='number' class='form-control' placeholder='#0' value={query.ratingLow || ''} onChange={(event) => handleChange(event, 'ratingLow')} />
          </div>
          <label>to</label>
          <div class='col'>
            <input type='number' class='form-control' placeholder='#10' value={query.ratingHigh || ''} onChange={(event) => handleChange(event, 'ratingHigh')} />
          </div>
        </div>
      </div>
    </form>
  )
}
