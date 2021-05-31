(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);var f=new Error("Cannot find module '"+o+"'");throw f.code="MODULE_NOT_FOUND",f}var l=n[o]={exports:{}};t[o][0].call(l.exports,function(e){var n=t[o][1][e];return s(n?n:e)},l,l.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({"./src/chosen.coffee":[function(require,module,exports){
var indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

module.exports = angular.module('angular-chosen', []);

angular.module('angular-chosen').directive('chosen', [
  '$timeout', function($timeout) {
    var CHOSEN_OPTION_WHITELIST, NG_OPTIONS_REGEXP, isEmpty, snakeCase;
    NG_OPTIONS_REGEXP = /^\s*(.*?)(?:\s+as\s+(.*?))?(?:\s+group\s+by\s+(.*))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+(.*?)(?:\s+track\s+by\s+(.*?))?$/;
    CHOSEN_OPTION_WHITELIST = ['noResultsText', 'allowSingleDeselect', 'disableSearchThreshold', 'disableSearch', 'enableSplitWordSearch', 'inheritSelectClasses', 'maxSelectedOptions', 'placeholderTextMultiple', 'placeholderTextSingle', 'searchContains', 'singleBackstrokeDelete', 'displayDisabledOptions', 'displaySelectedOptions', 'width'];
    snakeCase = function(input) {
      return input.replace(/[A-Z]/g, function($1) {
        return "_" + ($1.toLowerCase());
      });
    };
    isEmpty = function(value) {
      var key;
      if (angular.isArray(value)) {
        return value.length === 0;
      } else if (angular.isObject(value)) {
        for (key in value) {
          if (value.hasOwnProperty(key)) {
            return false;
          }
        }
      }
      return true;
    };
    return {
      restrict: 'A',
      require: '?ngModel',
      terminal: true,
      link: function(scope, element, attr, ngModel) {
        var chosen, defaultText, disableWithMessage, empty, initOrUpdate, match, options, origRender, removeEmptyMessage, startLoading, stopLoading, valuesExpr, viewWatch;
        element.addClass('angular-chosen');
        options = scope.$eval(attr.chosen) || {};
        angular.forEach(attr, function(value, key) {
          if (indexOf.call(CHOSEN_OPTION_WHITELIST, key) >= 0) {
            return options[snakeCase(key)] = scope.$eval(value);
          }
        });
        startLoading = function() {
          return element.addClass('loading').attr('disabled', true).trigger('chosen:updated');
        };
        stopLoading = function() {
          return element.removeClass('loading').attr('disabled', false).trigger('chosen:updated');
        };
        chosen = null;
        defaultText = null;
        empty = false;
        initOrUpdate = function() {
          if (chosen) {
            return element.trigger('chosen:updated');
          } else {
            chosen = element.chosen(options).data('chosen');
            if (angular.isObject(chosen)) {
              return defaultText = chosen.default_text;
            }
          }
        };
        removeEmptyMessage = function() {
          empty = false;
          return element.attr('data-placeholder', defaultText);
        };
        disableWithMessage = function() {
          empty = true;
          return element.attr('data-placeholder', chosen.results_none_found).attr('disabled', true).trigger('chosen:updated');
        };
        if (ngModel) {
          origRender = ngModel.$render;
          ngModel.$render = function() {
            origRender();
            return initOrUpdate();
          };
          if (attr.multiple) {
            viewWatch = function() {
              return ngModel.$viewValue;
            };
            scope.$watch(viewWatch, ngModel.$render, true);
          }
        } else {
          initOrUpdate();
        }
        attr.$observe('disabled', function() {
          return element.trigger('chosen:updated');
        });
        if (attr.ngOptions && ngModel) {
          match = attr.ngOptions.match(NG_OPTIONS_REGEXP);
          valuesExpr = match[7];
          scope.$watchCollection(valuesExpr, function(newVal, oldVal) {
            var timer;
            return timer = $timeout(function() {
              if (angular.isUndefined(newVal)) {
                return startLoading();
              } else {
                if (empty) {
                  removeEmptyMessage();
                }
                stopLoading();
                if (isEmpty(newVal)) {
                  return disableWithMessage();
                }
              }
            });
          });
          return scope.$on('$destroy', function(event) {
            if (typeof timer !== "undefined" && timer !== null) {
              return $timeout.cancel(timer);
            }
          });
        }
      }
    };
  }
]);



},{}]},{},["./src/chosen.coffee"])
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm5vZGVfbW9kdWxlcy9icm93c2VyaWZ5L25vZGVfbW9kdWxlcy9icm93c2VyLXBhY2svX3ByZWx1ZGUuanMiLCIvVXNlcnMvdml0YWx5L3Byb2plY3RzL2FuZ3VsYXItY2hvc2VuL3NyYy9jaG9zZW4uY29mZmVlIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0FDQUEsSUFBQSxtSkFBQTs7QUFBQSxNQUFNLENBQUMsT0FBUCxHQUFpQixPQUFPLENBQUMsTUFBUixDQUFlLGdCQUFmLEVBQWlDLEVBQWpDLENBQWpCLENBQUE7O0FBQUEsT0FFTyxDQUFDLE1BQVIsQ0FBZSxnQkFBZixDQUFnQyxDQUFDLFNBQWpDLENBQTJDLFFBQTNDLEVBQXFEO0VBQUMsVUFBRCxFQUFhLFNBQUMsUUFBRCxHQUFBO0FBR2hFLFFBQUEsOERBQUE7QUFBQSxJQUFBLGlCQUFBLEdBQW9CLDhLQUFwQixDQUFBO0FBQUEsSUFHQSx1QkFBQSxHQUEwQixDQUN4QixlQUR3QixFQUV4QixxQkFGd0IsRUFHeEIsd0JBSHdCLEVBSXhCLGVBSndCLEVBS3hCLHVCQUx3QixFQU14QixzQkFOd0IsRUFPeEIsb0JBUHdCLEVBUXhCLHlCQVJ3QixFQVN4Qix1QkFUd0IsRUFVeEIsZ0JBVndCLEVBV3hCLHdCQVh3QixFQVl4Qix3QkFad0IsRUFheEIsd0JBYndCLEVBY3hCLE9BZHdCLENBSDFCLENBQUE7QUFBQSxJQW9CQSxTQUFBLEdBQVksU0FBQyxLQUFELEdBQUE7YUFBVyxLQUFLLENBQUMsT0FBTixDQUFjLFFBQWQsRUFBd0IsU0FBQyxFQUFELEdBQUE7ZUFBUSxHQUFBLEdBQUcsQ0FBQyxFQUFFLENBQUMsV0FBSCxDQUFBLENBQUQsRUFBWDtNQUFBLENBQXhCLEVBQVg7SUFBQSxDQXBCWixDQUFBO0FBQUEsSUFxQkEsT0FBQSxHQUFVLFNBQUMsS0FBRCxHQUFBO0FBQ1IsVUFBQSxHQUFBO0FBQUEsTUFBQSxJQUFHLE9BQU8sQ0FBQyxPQUFSLENBQWdCLEtBQWhCLENBQUg7QUFDRSxl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QUVBLGdCQUFBLElBQXdCLE9BQUEsQ0FBUSxNQUFSLENBQXhCO3lCQUFBLGtCQUFBLENBQUEsRUFBQTtpQkFMRjtlQURlO1lBQUEsQ0FBVCxFQUZ5QjtVQUFBLENBQW5DLENBSEEsQ0FBQTtpQkFjQSxLQUFLLENBQUMsR0FBTixDQUFVLFVBQVYsRUFBc0IsU0FBQyxLQUFELEdBQUE7QUFDcEIsWUFBQSxJQUF5Qiw4Q0FBekI7cUJBQUEsUUFBUSxDQUFDLE1BQVQsQ0FBZ0IsS0FBaEIsRUFBQTthQURvQjtVQUFBLENBQXRCLEVBZkY7U0F4REk7TUFBQSxDQUhOO01BL0JnRTtFQUFBLENBQWI7Q0FBckQsQ0FGQSxDQUFBIiwiZmlsZSI6ImdlbmVyYXRlZC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzQ29udGVudCI6WyIoZnVuY3Rpb24gZSh0LG4scil7ZnVuY3Rpb24gcyhvLHUpe2lmKCFuW29dKXtpZighdFtvXSl7dmFyIGE9dHlwZW9mIHJlcXVpcmU9PVwiZnVuY3Rpb25cIiYmcmVxdWlyZTtpZighdSYmYSlyZXR1cm4gYShvLCEwKTtpZihpKXJldHVybiBpKG8sITApO3ZhciBmPW5ldyBFcnJvcihcIkNhbm5vdCBmaW5kIG1vZHVsZSAnXCIrbytcIidcIik7dGhyb3cgZi5jb2RlPVwiTU9EVUxFX05PVF9GT1VORFwiLGZ9dmFyIGw9bltvXT17ZXhwb3J0czp7fX07dFtvXVswXS5jYWxsKGwuZXhwb3J0cyxmdW5jdGlvbihlKXt2YXIgbj10W29dWzFdW2VdO3JldHVybiBzKG4/bjplKX0sbCxsLmV4cG9ydHMsZSx0LG4scil9cmV0dXJuIG5bb10uZXhwb3J0c312YXIgaT10eXBlb2YgcmVxdWlyZT09XCJmdW5jdGlvblwiJiZyZXF1aXJlO2Zvcih2YXIgbz0wO288ci5sZW5ndGg7bysrKXMocltvXSk7cmV0dXJuIHN9KSIsIm1vZHVsZS5leHBvcnRzID0gYW5ndWxhci5tb2R1bGUoJ2FuZ3VsYXItY2hvc2VuJywgW10pXG5cbmFuZ3VsYXIubW9kdWxlKCdhbmd1bGFyLWNob3NlbicpLmRpcmVjdGl2ZSAnY2hvc2VuJywgWyckdGltZW91dCcsICgkdGltZW91dCkgLT5cblxuICAjIFRoaXMgaXMgc3RvbGVuIGZyb20gQW5ndWxhci4uLlxuICBOR19PUFRJT05TX1JFR0VYUCA9IC9eXFxzKiguKj8pKD86XFxzK2FzXFxzKyguKj8pKT8oPzpcXHMrZ3JvdXBcXHMrYnlcXHMrKC4qKSk/XFxzK2ZvclxccysoPzooW1xcJFxcd11bXFwkXFx3XSopfCg/OlxcKFxccyooW1xcJFxcd11bXFwkXFx3XSopXFxzKixcXHMqKFtcXCRcXHddW1xcJFxcd10qKVxccypcXCkpKVxccytpblxccysoLio/KSg/Olxccyt0cmFja1xccytieVxccysoLio/KSk/JC9cblxuICAjIFdoaXRlbGlzdCBvZiBvcHRpb25zIHRoYXQgd2lsbCBiZSBwYXJzZWQgZnJvbSB0aGUgZWxlbWVudCdzIGF0dHJpYnV0ZXMgYW5kIHBhc3NlZCBpbnRvIENob3NlblxuICBDSE9TRU5fT1BUSU9OX1dISVRFTElTVCA9IFtcbiAgICAnbm9SZXN1bHRzVGV4dCdcbiAgICAnYWxsb3dTaW5nbGVEZXNlbGVjdCdcbiAgICAnZGlzYWJsZVNlYXJjaFRocmVzaG9sZCdcbiAgICAnZGlzYWJsZVNlYXJjaCdcbiAgICAnZW5hYmxlU3BsaXRXb3JkU2VhcmNoJ1xuICAgICdpbmhlcml0U2VsZWN0Q2xhc3NlcydcbiAgICAnbWF4U2VsZWN0ZWRPcHRpb25zJ1xuICAgICdwbGFjZWhvbGRlclRleHRNdWx0aXBsZSdcbiAgICAncGxhY2Vob2xkZXJUZXh0U2luZ2xlJ1xuICAgICdzZWFyY2hDb250YWlucydcbiAgICAnc2luZ2xlQmFja3N0cm9rZURlbGV0ZSdcbiAgICAnZGlzcGxheURpc2FibGVkT3B0aW9ucydcbiAgICAnZGlzcGxheVNlbGVjdGVkT3B0aW9ucydcbiAgICAnd2lkdGgnXG4gIF1cblxuICBzbmFrZUNhc2UgPSAoaW5wdXQpIC0+IGlucHV0LnJlcGxhY2UgL1tBLVpdL2csICgkMSkgLT4gXCJfI3skMS50b0xvd2VyQ2FzZSgpfVwiXG4gIGlzRW1wdHkgPSAodmFsdWUpIC0+XG4gICAgaWYgYW5ndWxhci5pc0FycmF5KHZhbHVlKVxuICAgICAgcmV0dXJuIHZhbHVlLmxlbmd0aCBpcyAwXG4gICAgZWxzZSBpZiBhbmd1bGFyLmlzT2JqZWN0KHZhbHVlKVxuICAgICAgcmV0dXJuIGZhbHNlIGZvciBrZXkgb2YgdmFsdWUgd2hlbiB2YWx1ZS5oYXNPd25Qcm9wZXJ0eShrZXkpXG4gICAgdHJ1ZVxuXG4gIHJlc3RyaWN0OiAnQSdcbiAgcmVxdWlyZTogJz9uZ01vZGVsJ1xuICB0ZXJtaW5hbDogdHJ1ZVxuICBsaW5rOiAoc2NvcGUsIGVsZW1lbnQsIGF0dHIsIG5nTW9kZWwpIC0+XG5cbiAgICBlbGVtZW50LmFkZENsYXNzKCdhbmd1bGFyLWNob3NlbicpXG5cbiAgICAjIFRha2UgYSBoYXNoIG9mIG9wdGlvbnMgZnJvbSB0aGUgY2hvc2VuIGRpcmVjdGl2ZVxuICAgIG9wdGlvbnMgPSBzY29wZS4kZXZhbChhdHRyLmNob3Nlbikgb3Ige31cblxuICAgICMgT3B0aW9ucyBkZWZpbmVkIGFzIGF0dHJpYnV0ZXMgdGFrZSBwcmVjZWRlbmNlXG4gICAgYW5ndWxhci5mb3JFYWNoIGF0dHIsICh2YWx1ZSwga2V5KSAtPlxuICAgICAgb3B0aW9uc1tzbmFrZUNhc2Uoa2V5KV0gPSBzY29wZS4kZXZhbCh2YWx1ZSkgaWYga2V5IGluIENIT1NFTl9PUFRJT05fV0hJVEVMSVNUXG5cbiAgICBzdGFydExvYWRpbmcgPSAtPiBlbGVtZW50LmFkZENsYXNzKCdsb2FkaW5nJykuYXR0cignZGlzYWJsZWQnLCB0cnVlKS50cmlnZ2VyKCdjaG9zZW46dXBkYXRlZCcpXG4gICAgc3RvcExvYWRpbmcgPSAtPiBlbGVtZW50LnJlbW92ZUNsYXNzKCdsb2FkaW5nJykuYXR0cignZGlzYWJsZWQnLCBmYWxzZSkudHJpZ2dlcignY2hvc2VuOnVwZGF0ZWQnKVxuXG4gICAgY2hvc2VuID0gbnVsbFxuICAgIGRlZmF1bHRUZXh0ID0gbnVsbFxuICAgIGVtcHR5ID0gZmFsc2VcblxuICAgIGluaXRPclVwZGF0ZSA9IC0+XG4gICAgICBpZiBjaG9zZW5cbiAgICAgICAgZWxlbWVudC50cmlnZ2VyKCdjaG9zZW46dXBkYXRlZCcpXG4gICAgICBlbHNlXG4gICAgICAgIGNob3NlbiA9IGVsZW1lbnQuY2hvc2VuKG9wdGlvbnMpLmRhdGEoJ2Nob3NlbicpXG4gICAgICAgIGlmIGFuZ3VsYXIuaXNPYmplY3QoY2hvc2VuKVxuICAgICAgICAgIGRlZmF1bHRUZXh0ID0gY2hvc2VuLmRlZmF1bHRfdGV4dFxuXG4gICAgIyBVc2UgQ2hvc2VuJ3MgcGxhY2Vob2xkZXIgb3Igbm8gcmVzdWx0cyBmb3VuZCB0ZXh0IGRlcGVuZGluZyBvbiB3aGV0aGVyIHRoZXJlIGFyZSBvcHRpb25zIGF2YWlsYWJsZVxuICAgIHJlbW92ZUVtcHR5TWVzc2FnZSA9IC0+XG4gICAgICBlbXB0eSA9IGZhbHNlXG4gICAgICBlbGVtZW50LmF0dHIoJ2RhdGEtcGxhY2Vob2xkZXInLCBkZWZhdWx0VGV4dClcblxuICAgIGRpc2FibGVXaXRoTWVzc2FnZSA9IC0+XG4gICAgICBlbXB0eSA9IHRydWVcbiAgICAgIGVsZW1lbnQuYXR0cignZGF0YS1wbGFjZWhvbGRlcicsIGNob3Nlbi5yZXN1bHRzX25vbmVfZm91bmQpLmF0dHIoJ2Rpc2FibGVkJywgdHJ1ZSkudHJpZ2dlcignY2hvc2VuOnVwZGF0ZWQnKVxuXG4gICAgIyBXYXRjaCB0aGUgdW5kZXJseWluZyBuZ01vZGVsIGZvciB1cGRhdGVzIGFuZCB0cmlnZ2VyIGFuIHVwZGF0ZSB3aGVuIHRoZXkgb2NjdXIuXG4gICAgaWYgbmdNb2RlbFxuICAgICAgb3JpZ1JlbmRlciA9IG5nTW9kZWwuJHJlbmRlclxuICAgICAgbmdNb2RlbC4kcmVuZGVyID0gLT5cbiAgICAgICAgb3JpZ1JlbmRlcigpXG4gICAgICAgIGluaXRPclVwZGF0ZSgpXG5cbiAgICAgICMgVGhpcyBpcyBiYXNpY2FsbHkgdGFrZW4gZnJvbSBhbmd1bGFyIG5nT3B0aW9ucyBzb3VyY2UuICBuZ01vZGVsIHdhdGNoZXMgcmVmZXJlbmNlLCBub3QgdmFsdWUsXG4gICAgICAjIHNvIHdoZW4gdmFsdWVzIGFyZSBhZGRlZCBvciByZW1vdmVkIGZyb20gYXJyYXkgbmdNb2RlbHMsICRyZW5kZXIgd29uJ3QgYmUgZmlyZWQuXG4gICAgICBpZiBhdHRyLm11bHRpcGxlXG4gICAgICAgIHZpZXdXYXRjaCA9IC0+IG5nTW9kZWwuJHZpZXdWYWx1ZVxuICAgICAgICBzY29wZS4kd2F0Y2ggdmlld1dhdGNoLCBuZ01vZGVsLiRyZW5kZXIsIHRydWVcbiAgICAjIElmIHdlJ3JlIG5vdCB1c2luZyBuZ01vZGVsIChhbmQgdGhlcmVmb3JlIGFsc28gbm90IHVzaW5nIG5nT3B0aW9ucywgd2hpY2ggcmVxdWlyZXMgbmdNb2RlbCksXG4gICAgIyBqdXN0IGluaXRpYWxpemUgY2hvc2VuIGltbWVkaWF0ZWx5IHNpbmNlIHRoZXJlJ3Mgbm8gbmVlZCB0byB3YWl0IGZvciBuZ09wdGlvbnMgdG8gcmVuZGVyIGZpcnN0XG4gICAgZWxzZSBpbml0T3JVcGRhdGUoKVxuXG4gICAgIyBXYXRjaCB0aGUgZGlzYWJsZWQgYXR0cmlidXRlIChjb3VsZCBiZSBzZXQgYnkgbmdEaXNhYmxlZClcbiAgICBhdHRyLiRvYnNlcnZlICdkaXNhYmxlZCcsIC0+IGVsZW1lbnQudHJpZ2dlcignY2hvc2VuOnVwZGF0ZWQnKVxuXG4gICAgIyBXYXRjaCB0aGUgY29sbGVjdGlvbiBpbiBuZ09wdGlvbnMgYW5kIHVwZGF0ZSBjaG9zZW4gd2hlbiBpdCBjaGFuZ2VzLiAgVGhpcyB3b3JrcyB3aXRoIHByb21pc2VzIVxuICAgICMgbmdPcHRpb25zIGRvZXNuJ3QgZG8gYW55dGhpbmcgdW5sZXNzIHRoZXJlIGlzIGFuIG5nTW9kZWwsIHNvIG5laXRoZXIgZG8gd2UuXG4gICAgaWYgYXR0ci5uZ09wdGlvbnMgYW5kIG5nTW9kZWxcbiAgICAgIG1hdGNoID0gYXR0ci5uZ09wdGlvbnMubWF0Y2goTkdfT1BUSU9OU19SRUdFWFApXG4gICAgICB2YWx1ZXNFeHByID0gbWF0Y2hbN11cblxuICAgICAgc2NvcGUuJHdhdGNoQ29sbGVjdGlvbiB2YWx1ZXNFeHByLCAobmV3VmFsLCBvbGRWYWwpIC0+XG4gICAgICAgICMgRGVmZXIgZXhlY3V0aW9uIHVudGlsIERPTSBpcyBsb2FkZWRcbiAgICAgICAgdGltZXIgPSAkdGltZW91dCgtPlxuICAgICAgICAgIGlmIGFuZ3VsYXIuaXNVbmRlZmluZWQobmV3VmFsKVxuICAgICAgICAgICAgc3RhcnRMb2FkaW5nKClcbiAgICAgICAgICBlbHNlXG4gICAgICAgICAgICByZW1vdmVFbXB0eU1lc3NhZ2UoKSBpZiBlbXB0eVxuICAgICAgICAgICAgc3RvcExvYWRpbmcoKVxuICAgICAgICAgICAgZGlzYWJsZVdpdGhNZXNzYWdlKCkgaWYgaXNFbXB0eShuZXdWYWwpXG4gICAgICAgIClcblxuICAgICAgc2NvcGUuJG9uICckZGVzdHJveScsIChldmVudCkgLT5cbiAgICAgICAgJHRpbWVvdXQuY2FuY2VsIHRpbWVyIGlmIHRpbWVyP1xuXVxuIl19
