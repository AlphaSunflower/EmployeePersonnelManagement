import request from './request'

export async function downloadFile(url, params, filename) {
  const blob = await request.get(url, {
    params,
    responseType: 'blob'
  })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(link.href)
}
