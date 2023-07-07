import React, { Component, ErrorInfo, ReactNode } from 'react'

interface Props {
  fallback?: ReactNode
  children?: ReactNode
}

interface State {
  hasError: boolean
  error: Error | null
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
  }

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error }
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error:', error, errorInfo)
  }

  public render() {
    if (this.state.hasError) {
      if (this.props.fallback) {
        return this.props.fallback
      }
      return (
        <>
          <h1>Sorry.. there was an error </h1>
          <h1>{this.state.error?.message}</h1>
        </>
      )
    }

    return this.props.children
  }
}

export default ErrorBoundary
