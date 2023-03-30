import React, { useCallback, useState } from 'react'
import { concat, of, Subject } from 'rxjs'
import TextField from '@material-ui/core/TextField'
import Button from '@material-ui/core/Button'
import { bind, SUSPENSE } from '@react-rxjs/core'
import { map, switchMap } from 'rxjs/operators'
import Typography from '@material-ui/core/Typography'
import { EchoRequest, EchoService } from '../../generated/WebGateway'

const echoRequest$ = new Subject<EchoRequest>()

const suspensefulResponse$ = echoRequest$.pipe(
  switchMap(request =>
    concat(
      of(SUSPENSE),
      EchoService.echo(request).pipe(map(response => response.body))
    )
  )
)

const [useResponse] = bind(concat(of(''), suspensefulResponse$))

export const Response = () => {
  const response = useResponse()
  return <Typography paragraph>{response}</Typography>
}

export const Loading = () => <Typography paragraph>Loading...</Typography>

export const EchoPage = () => {
  const [value, setValue] = useState('')

  const handleValueChange = useCallback(
    e => setValue(e.target.value),
    [setValue]
  )

  const handleSubmit = useCallback(
    e => {
      e.preventDefault()
      echoRequest$.next({
        body: value,
      })
    },
    [value]
  )

  return (
    <>
      <form noValidate autoComplete="off" onSubmit={handleSubmit}>
        <TextField value={value} onChange={handleValueChange} />
        <Button type="submit" variant="contained">
          Submit
        </Button>
      </form>
      <React.Suspense fallback={<Loading />}>
        <Response />
      </React.Suspense>
    </>
  )
}
