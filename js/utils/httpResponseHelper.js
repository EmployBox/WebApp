export async function checkAndParseResponse (resp) {
  let parsed = JSON.parse(await resp.text())

  if (!resp.ok) throw new Error(`Error ${parsed.status}. ${parsed.detail}`)
  return parsed
}
