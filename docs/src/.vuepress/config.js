import {defineUserConfig} from 'vuepress'
import {defaultTheme} from 'vuepress'

export default defineUserConfig({
    base: '/',
    lang: 'en-US',
    title: 'ZeroAuth',
    description: 'Multi-platform zkLogin Suite',
    theme: defaultTheme({
        logo: "/zero-light.svg",
        logoDark: "/zero-dark.svg",
        contributors: false,
        navbar: [
            {text: "zkLogin", link: "/zklogin"},
            {
                text: "Languages", children: [
                    {text: "English", link: "/"}
                ]
            },
            {text: "Contribute", link: "https://github.com/mcxross/zeroauth"},
        ]
    })
})
