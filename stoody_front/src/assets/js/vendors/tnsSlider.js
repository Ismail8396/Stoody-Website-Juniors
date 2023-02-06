(function () {

  // slider first

  if ($('.sliderFirst').length) {

    var slider = tns({
      container: '.sliderFirst',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderFirstControls",
      responsive: {
        768: {
          items: 2
        },

        990: {
          items: 4
        }
      }

    });

  }


  // slider second

  if ($('.sliderSecond').length) {
    var slider = tns({
      container: '.sliderSecond',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderSecondControls",
      responsive: {
        768: {
          items: 2
        },
        990: {
          items: 4
        }
      }

    });
  }



  // slider third
  if ($('.sliderThird').length) {
    var slider = tns({
      container: '.sliderThird',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderThirdControls",
      responsive: {
        768: {
          items: 2
        },
        990: {
          items: 4
        }
      }

    });


  }



  // slider fourth
  if ($('.sliderFourth').length) {
    var slider = tns({
      container: '.sliderFourth',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderFourthControls",
      responsive: {
        768: {
          items: 2
        },
        990: {
          items: 4
        }
      }

    });


  }


  // slider testimonial
  if ($('.sliderTestimonial').length) {
    var slider = tns({
      container: '.sliderTestimonial',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderTestimonialControls",
      responsive: {
        768: {
          items: 2
        },
        990: {
          items: 3
        }
      }

    });


  }
  


  // slider testimonial
  if ($('.sliderTestimonialSecond').length) {
    var slider = tns({
      container: '.sliderTestimonialSecond',
      loop: true,
      startIndex: 1,
      items: 1,
      nav: false,
      autoplay: true,
      swipeAngle: false,
      speed: 400,
      autoplayButtonOutput: false,
      mouseDrag: true,
      lazyload: true,
      gutter: 20,
      controlsContainer: "#sliderTestimonialSecondControls",
      responsive: {
        768: {
          items: 1
        },
        990: {
          items: 1
        }
      }

    });


  }


  // slider for product
  if ($('.product').length) {
    var slider = tns({
      "container": "#product",
      "items": 1,
      "startIndex": 1,
      "navContainer": "#product-thumbnails",
      "navAsThumbnails": true,
      "autoplay": false,
      "autoplayTimeout": 1000,
      "swipeAngle": false,
      "speed": 400,
      "controls": false,

    });


  }


 // slider testimonial
 if ($('.sliderTestimonialThird').length) {
  var slider = tns({
    container: '.sliderTestimonialThird',
    loop: true,
    startIndex: 1,
    items: 1,
    nav: false,
    autoplay: true,
    swipeAngle: false,
    speed: 400,
    autoplayButtonOutput: false,
    mouseDrag: true,
    lazyload: true,
    gutter: 20,
    controlsContainer: "#sliderTestimonialThirdControls",
    responsive: {
      768: {
        items: 2
      },
      990: {
        items: 3
      }
    }

  });


}



})();