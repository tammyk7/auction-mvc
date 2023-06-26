import { rest } from 'msw'
import { setupServer } from 'msw/node'
import { Subscribe } from '@react-rxjs/core'
import { render, fireEvent } from '@testing-library/react'

import Characters from '../pages/Characters'

const server = setupServer(
  rest.get(`https://swapi.dev/api/people`, (_, res, ctx) => {
    return res(
      ctx.json({
        count: 4,
        next: null,
        previous: null,
        results: [
          {
            name: 'Luke Skywalker',
            url: 'https://swapi.dev/api/people/1/',
          },
          {
            name: 'Darth Vader',
            url: 'https://swapi.dev/api/people/4/',
          },
        ],
      })
    )
  }),
  rest.get(`https://swapi.dev/api/people/1`, (_, res, ctx) => {
    return res(
      ctx.json({
        name: 'Luke Skywalker',
        height: '172',
        hair_color: 'blond',
        eye_color: 'blue',
        birth_year: '19BBY',
        url: 'https://swapi.dev/api/people/1/',
      })
    )
  })
)

beforeAll(() => server.listen())
afterEach(() => server.resetHandlers())
afterAll(() => server.close())

test('renders characters list', async () => {
  const { findByText } = render(
    <Subscribe>
      <Characters />
    </Subscribe>
  )

  expect(await findByText('Luke Skywalker')).toBeInTheDocument()
})

test('renders character modal on character click', async () => {
  const { findByText, getByText } = render(
    <Subscribe>
      <Characters />
    </Subscribe>
  )

  await findByText('Luke Skywalker')

  const characterElement = getByText('Luke Skywalker')

  fireEvent.click(characterElement)

  expect(await findByText('Born:')).toBeInTheDocument()
})
