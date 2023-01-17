import { SimplePaletteColorOptions } from '@material-ui/core'
import { PaletteOptions } from '@material-ui/core/styles/createPalette'
import merge from 'lodash/merge'

/**
 * Core UI Colors - these are extracted and named from the design system.
 * They are mapped to the theme palette below (see basePalette, darkPaletteOverrides, etc)
 */

const BRAND = '#21B357'
const BRAND_LIGHT = '#4DC38F'
const BRAND_DARK = '#1A905c'

const PRIMARY: SimplePaletteColorOptions = {
  main: BRAND,
  light: BRAND_LIGHT,
  dark: BRAND_DARK,
  contrastText: '#FFFFFF',
}

const BRAND_SECONDARY = '#262626'
const SECONDARY: SimplePaletteColorOptions = {
  main: BRAND_SECONDARY,
  dark: BRAND_DARK,
}
/**
 * Map the design system colors to the theme conventions
 */
const basePalette: PaletteOptions = {}

const darkPaletteOverrides: PaletteOptions = {
  type: 'dark',
  primary: PRIMARY,
  secondary: SECONDARY,
  action: {
    disabledBackground: BRAND_SECONDARY,
  },
}

export const darkPalette = merge({}, darkPaletteOverrides)

const lightPaletteOverrides: PaletteOptions = {
  type: 'light',
}

export const lightPalette = merge({}, basePalette, lightPaletteOverrides)
