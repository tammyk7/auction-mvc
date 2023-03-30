import { bind } from '@react-rxjs/core'
import * as HydraPlatform from '@adaptive/hydra-platform'
import { AsyncSubject, connectable } from 'rxjs'
import { connectionStatus$ } from '@adaptive/hydra-platform'
import { checkCompatibility } from '../generated/WebGateway'

export const [useConnectionStatus] = bind(connectionStatus$())

const compatibility$ = connectable(checkCompatibility(), {
  connector: () =>
    new AsyncSubject<HydraPlatform.VersionNegotiation.Compatibility>(),
  resetOnDisconnect: false,
})
compatibility$.connect()
export const [useAdminGWCompatibility] = bind(compatibility$)
