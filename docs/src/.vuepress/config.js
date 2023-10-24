import {defineUserConfig} from 'vuepress'
import {defaultTheme} from 'vuepress'
import {head, navbarEn, sidebarEn} from "./configs";
import {docsearchPlugin} from "@vuepress/plugin-docsearch";
import {googleAnalyticsPlugin} from "@vuepress/plugin-google-analytics";

export default defineUserConfig({
    base: '/',
    lang: 'en-US',
    title: 'ZeroAuth',
    description: 'Simple, multi-platform zkLogin for your (d)app',
    head: head,
    theme: defaultTheme({
        locales: {
            '/': {
                logo: "/zero-light.svg",
                logoDark: "/zero-dark.svg",
                contributors: false,
                navbar: navbarEn,
                sidebar: sidebarEn,
            },
            '/zh/': {}
        }

    }),
    plugins: [
        googleAnalyticsPlugin({
            id: 'G-MJYPLNJ8LF',
            debug: true
        }),
        docsearchPlugin({
            appId: 'JH481W5ZQ4',
            apiKey: '8e1a51997283b7eb0ef6f647ea0e7158',
            indexName: 'zeroauth',
        }),
    ]
})
