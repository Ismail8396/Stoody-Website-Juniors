// Load plugins
const { src, dest, watch, parallel, series } = require('gulp')
const sass = require('gulp-sass')(require('sass'))
const gulpautoprefixer = require('gulp-autoprefixer')
const browsersync = require('browser-sync').create()
const fileinclude = require('gulp-file-include')
const useref = require('gulp-useref')
const cached = require('gulp-cached')
const gulpIf = require('gulp-if')
const del = require('del')
const npmDist = require('gulp-npm-dist')
const postcss = require('gulp-postcss')
const cssnano = require('cssnano')
const autoprefixer = require('autoprefixer')
const replace = require('gulp-replace')
const gulpTerser = require('gulp-terser')
const sourcemaps = require('gulp-sourcemaps')
const uglify = require('gulp-uglify')
const rename = require('gulp-rename')
const terser = require('terser')
const concat = require('gulp-concat')

// Variables
const fileincludeOptions = {
	prefix: '@@',
	basepath: '@file'
}

// Paths to project folders
const paths = {
    base: {
      	base: './',
        node: './node_modules'
    },
    src: {
        basesrc: './src',
        basesrcfiles: './src/**/*',
        scss: './src/assets/scss/**/*.scss',
        css: './src/assets/css/**/*.css',
        cssCopy: './src/assets/css',
        js: './src/assets/js/**/*.js',
        jsCopy: './src/assets/js/*.js',
        vendorJs: './src/assets/js/vendors/*.js',
        html: './src/**/*.html',
        images: './src/assets/images/**/*',
        fonts: './src/assets/fonts/**/*',
        assets: './src/assets/**/*',
        partials: '.src/partials/**/*'
    },
    temp: {
        basetemp: './.temp'
    },
    dist: {
        basedist: './dist',
        copyAssets: './dist/templates/assets',
        js: './dist/static/assets/js',
        vendorJs: './dist/static/assets/js/vendors',
        images: './dist/static/assets/images',
        css: './dist/static/assets/css',
        cssCopy: './dist/templates/assets/css/**/*.css',
        fonts: './dist/static/assets/fonts',
        libs: './dist/static/assets/libs',
        html: './dist/templates'
    }
}

// SCSS to CSS
const scss = () =>{
    return src(paths.src.scss)
        .pipe(sourcemaps.init())
        .pipe(sass().on('error', sass.logError))
        .pipe(gulpautoprefixer())
        .pipe(sourcemaps.write('.'))
        .pipe(dest(paths.src.cssCopy))
        .pipe(browsersync.stream())
}

//CSS
const css = () => {
    return src(paths.src.css)
      .pipe(gulpIf('*.css', postcss([cssnano(), autoprefixer()])))
      .pipe(rename({ suffix: '.min' }))
      .pipe(dest(paths.dist.css))
}

// vendor js
const vendorJs = () => {
    return src(paths.src.vendorJs)
        .pipe(uglify())
        .pipe(dest(paths.dist.vendorJs))
}

// Image
const images = () => {
    return src(paths.src.images)
        .pipe(dest(paths.dist.images))
}

// Font task
const fonts = () => {
    return src(paths.src.fonts)
        .pipe(dest(paths.dist.fonts))
}

// HTML
const html = () => {
    const stream = src(paths.src.html)
      .pipe(fileinclude(fileincludeOptions))
      .pipe(useref({searchPath: paths.src.html}))
      .pipe(replace(/ src="(.{0,10})node_modules/g, ' src="$1assets/libs'))
      .pipe(replace(/ href="(.{0,10})node_modules/g, ' href="$1assets/libs'))
      .pipe(replace(/ href=".\//g, ' href="'))
      .pipe(replace(/ href="(.{0,14})assets/g, ' href="../$1static/assets'))
      .pipe(replace(/ src=".\//g, ' src="'))
      .pipe(replace(/ src="(.{0,14})assets/g, ' src="../$1static/assets'))
      .pipe(cached())
      .pipe(dest(paths.dist.html))
      .pipe(browsersync.stream())

    return stream
}

//JS
const js = () => {
    return src(paths.src.jsCopy)
        .pipe(gulpIf('*.js', gulpTerser({}, terser.minify)))
        .pipe(concat('theme.min.js'))
        .pipe(dest(paths.dist.js))
}

// File include task for temp
const fileincludeTask = () => {
    return src(paths.src.html)
        .pipe(fileinclude(fileincludeOptions))
        .pipe(cached())
        .pipe(dest(paths.temp.basetemp))
}

// Copy libs file from nodemodules to dist
const copyLibs = () => {
    return src(npmDist(), { base: paths.base.node })
        .pipe(dest(paths.dist.libs))
}

// Clean Dist folder
const cleanDist = (cb) => {
    del.sync(paths.dist.basedist)
    del.sync(paths.dist.basedist)
    del.sync(paths.dist.basedist)
    cb()
}

// Browser Sync Serve
const browsersyncServe = (cb) => {
    browsersync.init({
        server: {
            baseDir: [paths.temp.basetemp, paths.src.basesrc, paths.base.base]
        }
    })
    cb()
}

// SyncReload
const syncReload = (cb) => {
    browsersync.reload()
    cb()
}

// Watch Task
const watchTask = (cb) => {
    watch(paths.src.html, series(fileincludeTask, syncReload))
    watch([paths.src.images, paths.src.fonts, paths.src.vendorJs], series(images, fonts, vendorJs))
    watch([paths.src.scss], series(scss, syncReload))
    cb()
}

// Default Task Preview
exports.default = series(fileincludeTask, browsersyncServe, watchTask)

// Build Task for Dist
exports.build = series(parallel(cleanDist), html, css, js, images, fonts, vendorJs, copyLibs)

// export tasks
exports.scss = scss
exports.vendorJs = vendorJs
exports.images = images
exports.fonts = fonts
exports.html = html
exports.css = css
exports.fileincludeTask = fileincludeTask
exports.copyLibs = copyLibs
exports.cleanDist = cleanDist