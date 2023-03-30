import AppBar from '@material-ui/core/AppBar'
import Divider from '@material-ui/core/Divider'
import Drawer from '@material-ui/core/Drawer'
import IconButton from '@material-ui/core/IconButton'
import List from '@material-ui/core/List'
import ListItem from '@material-ui/core/ListItem'
import ListItemIcon from '@material-ui/core/ListItemIcon'
import ListItemText from '@material-ui/core/ListItemText'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import ChevronRightIcon from '@material-ui/icons/ChevronRight'
import MailIcon from '@material-ui/icons/Mail'
import MenuIcon from '@material-ui/icons/Menu'
import InboxIcon from '@material-ui/icons/MoveToInbox'
import React from 'react'
import styled from 'styled-components/macro'
import { EchoPage } from './pages/echo'
import { useAdminGWCompatibility } from './hooks/useConnectionStatus'

const drawerWidth = '240px'

type Open = {
  open: boolean
}

const Content = styled('div')<Open>`
  margin-left: 0;
  padding: ${({ theme }) => theme.spacing(3)}px;
  transition: ${({ theme }) =>
    theme.transitions.create(['margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    })};
  ${({ open, theme }) =>
    open &&
    `
    margin-left: ${drawerWidth};
    transition: ${theme.transitions.create(['margin'], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    })};
  `}
`

const StyledAppBar = styled(AppBar)<Open>`
  z-index: ${({ theme }) => theme.zIndex.drawer + 1};
  transition: ${({ theme }) =>
    theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    })};
  ${({ open, theme }) =>
    open &&
    `
    width: calc(100% - ${drawerWidth});
    margin-left: ${drawerWidth};
    transition: ${theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    })};
  `}
`

const DrawerHeader = styled('div')`
  display: flex;
  align-items: center;
  padding: ${({ theme }) => theme.spacing(0, 1)}px;
  justify-content: flex-end;
  ${({ theme }) => theme.mixins.toolbar as any}
`

const StyledDrawer = styled(Drawer)`
  width: ${drawerWidth};
  flex-shrink: 0;
  .MuiDrawer-paper {
    width: ${drawerWidth};
  }
`

const StyledIconButton = styled(IconButton)<Open>`
  margin-right: ${({ theme }) => theme.spacing(2)}px;
  ${({ open }) =>
    open &&
    `
    display: hidden;
`}
`

const Todo = () => (
  <Typography paragraph>
    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
    tempor incididunt ut labore et dolore magna aliqua. Rhoncus dolor purus non
    enim praesent elementum facilisis leo vel. Risus at ultrices mi tempus
    imperdiet. Semper risus in hendrerit gravida rutrum quisque non tellus.
    Convallis convallis tellus id interdum velit laoreet id donec ultrices. Odio
    morbi quis commodo odio aenean sed adipiscing. Amet nisl suscipit adipiscing
    bibendum est ultricies integer quis. Cursus euismod quis viverra nibh cras.
    Metus vulputate eu scelerisque felis imperdiet proin fermentum leo. Mauris
    commodo quis imperdiet massa tincidunt. Cras tincidunt lobortis feugiat
    vivamus at augue. At augue eget arcu dictum varius duis at consectetur
    lorem. Velit sed ullamcorper morbi tincidunt. Lorem donec massa sapien
    faucibus et molestie ac.
  </Typography>
)

interface DrawerItemProps {
  text: string
  onClick: () => void
  icon: JSX.Element
}

const DrawerItem = ({ text, onClick, icon }: DrawerItemProps) => (
  <ListItem button key={text} onClick={onClick}>
    <ListItemIcon>{icon}</ListItemIcon>
    <ListItemText primary={text} />
  </ListItem>
)

export function App() {
  const [open, setOpen] = React.useState(false)
  const [tab, setTab] = React.useState(<EchoPage />)
  const compatibility = useAdminGWCompatibility()

  if (compatibility.type === 'incompatible') {
    const mismatches = compatibility.payload.methods.map(
      ({ method, reason }) => (
        <li key={method.methodRouteKey.toString()}>
          {method.serviceName}.{method.methodName}: {reason.type}
        </li>
      )
    )

    return (
      <>
        <h1>Server is incompatible with client</h1>
        <ul>{mismatches}</ul>
      </>
    )
  }

  const handleDrawerOpen = () => {
    setOpen(true)
  }

  const handleDrawerClose = () => {
    setOpen(false)
  }

  return (
    <>
      <StyledAppBar position="fixed" open={open}>
        <Toolbar>
          <StyledIconButton
            open={open}
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
          >
            <MenuIcon />
          </StyledIconButton>
          <Typography variant="h6" noWrap>
            Admin UI
          </Typography>
        </Toolbar>
      </StyledAppBar>

      <StyledDrawer anchor="left" open={open} variant="persistent">
        <DrawerHeader>
          <IconButton onClick={handleDrawerClose}>
            <ChevronRightIcon />
          </IconButton>
        </DrawerHeader>
        <Divider />
        <List>
          <DrawerItem
            text="Echo"
            onClick={() => setTab(<EchoPage />)}
            icon={<MailIcon />}
          />
          <DrawerItem
            text="Chat Room"
            onClick={() => setTab(<Todo />)}
            icon={<InboxIcon />}
          />
        </List>
      </StyledDrawer>
      <Content open={open}>
        <DrawerHeader />
        {tab}
      </Content>
    </>
  )
}
