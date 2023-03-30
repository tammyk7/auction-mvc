import React from 'react'
import { ThemeProvider as StyledThemeProvider } from 'styled-components'
import {
  ThemeProvider as MuiThemeProvider,
  Theme,
  StylesProvider,
  createMuiTheme,
} from '@material-ui/core/styles'
import GlobalStyle from './globalStyle'
import { dark, darkThemeOptions, lightThemeOptions } from './configureTheme'

interface ThemeContextValue {
  theme: Theme
  setTheme: (theme: Theme) => void
}

const ThemeContext = React.createContext<ThemeContextValue>({
  theme: dark,
  setTheme: () => console.warn('Missing ThemeContextProvider'),
})

type ThemeProviderProps = {
  theme?: Theme
  children: React.ReactNode
}

export const ThemeProvider = ({
  children,
  theme: defaultTheme,
}: ThemeProviderProps) => {
  const [theme, setTheme] = React.useState(defaultTheme || dark)

  return (
    <StylesProvider injectFirst>
      <GlobalStyle />
      <MuiThemeProvider theme={theme}>
        <StyledThemeProvider theme={theme}>
          <ThemeContext.Provider value={{ theme, setTheme }}>
            {children}
          </ThemeContext.Provider>
        </StyledThemeProvider>
      </MuiThemeProvider>
    </StylesProvider>
  )
}

export function useTheme() {
  const { theme, setTheme } = React.useContext(ThemeContext)
  const toggleTheme = () =>
    setTheme(
      theme.palette.type === 'dark'
        ? createMuiTheme(lightThemeOptions)
        : createMuiTheme(darkThemeOptions)
    )
  return { theme, setTheme, toggleTheme }
}
