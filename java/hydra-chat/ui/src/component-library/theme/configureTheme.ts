import { createMuiTheme, ThemeOptions } from '@material-ui/core'
import { darkPalette, lightPalette } from './colors'
import { typography } from './typography'

export const darkThemeOptions: ThemeOptions = {
  palette: darkPalette,
  shape: {
    borderRadius: 3,
  },
  props: {
    MuiButton: {
      variant: 'contained',
      color: 'primary',
    },
  },
  overrides: {
    MuiButton: {
      root: {
        padding: '6 9',
      },
    },
  },
  typography,
}

export const lightThemeOptions: ThemeOptions = {
  palette: lightPalette,
}

export const dark = createMuiTheme(darkThemeOptions)
export const light = createMuiTheme(lightThemeOptions)
