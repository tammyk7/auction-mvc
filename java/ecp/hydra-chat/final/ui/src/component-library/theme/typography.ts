import { TypographyOptions } from '@material-ui/core/styles/createTypography'

const FONT_WEIGHT_BOLD = 500
const FONT_WEIGHT_LIGHT = 300

export const typography: TypographyOptions = {
  fontSize: 11,
  fontWeightBold: FONT_WEIGHT_BOLD,
  fontWeightLight: FONT_WEIGHT_LIGHT,
  h1: {
    fontSize: 19,
  },
  h2: {
    fontSize: 16,
  },
  h3: {
    fontSize: 13,
    fontWeight: FONT_WEIGHT_BOLD,
  },
  h4: {
    fontSize: 10,
    fontWeight: FONT_WEIGHT_BOLD,
  },
  subtitle1: {
    fontSize: 10,
    fontWeight: FONT_WEIGHT_BOLD,
  },
  body1: {},
  body2: {},
  button: {
    fontWeight: FONT_WEIGHT_BOLD,
    textTransform: 'none',
    fontSize: 11,
  },
  caption: {
    fontWeight: FONT_WEIGHT_LIGHT,
    fontSize: 'italic',
  },
}
