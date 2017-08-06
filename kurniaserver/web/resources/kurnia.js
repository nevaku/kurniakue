$(function () {
    new Vue({
        el: '#app',
        data: {
            message: 'Hello Vue.js!'
        },
        methods: {
            reverseMessage: function () {
                this.message = this.message.split('').reverse().join('');
            }
        }
    });

    new Vue({
        el: '#app-6',
        data: {
            message: 'Hello Vue!'
        }
    });

    Vue.component('todo-item', {
        // The todo-item component now accepts a
        // "prop", which is like a custom attribute.
        // This prop is called todo.
        props: ['todo'],
        template: '<li>{{ todo.text }}</li>'
    });
    
    new Vue({
        el: '#app-todo',
        data: {
            groceryList: [
                {id: 0, text: 'Vegetables'},
                {id: 1, text: 'Cheese'},
                {id: 2, text: 'Whatever else humans are supposed to eat'}
            ]
        }
    });
});