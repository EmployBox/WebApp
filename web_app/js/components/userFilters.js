import React from 'react'

export default (query, onQueryChange) => {
  function handleChange (event, property) {
    query[property] = event.target.value
    onQueryChange(query)
  }

  return (
    <form>
      <div class='form-group'>
        <label>Name</label>
        <input type='text' class='form-control' placeholder='User Name' value={query.name || ''} onChange={(event) => handleChange(event, 'name')} />
      </div>
      <div class='form-group'>
        <label>Rating from</label>
        <div class='row'>
          <div class='col'>
            <input type='number' class='form-control' value={query.ratingLow || ''} onChange={(event) => handleChange(event, 'ratingLow')} />
          </div>
          <label>to</label>
          <div class='col'>
            <input type='number' class='form-control' value={query.ratingHigh || ''} onChange={(event) => handleChange(event, 'ratingHigh')} />
          </div>
        </div>
      </div>
    </form>
  )
}
