/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        forest: {
          50: '#F1F7F4',
          100: '#DDECE4',
          200: '#BDDACD',
          300: '#94C1AE',
          500: '#2A7256',
          600: '#1F5B44',
          DEFAULT: '#174D38', // Primary
          700: '#174D38',
          800: '#113A2B',
          900: '#0B261C',
        },
        sage: {
          50: '#F5F8F4',
          100: '#E7EFE5',
          200: '#CFE0CB',
          300: '#B8D2B3',
          DEFAULT: '#A8C5A0', // Secondary
          400: '#A8C5A0',
          500: '#8FB086',
          600: '#75946D',
        },
        ivory: {
          DEFAULT: '#F8F7F0', // Background
          50: '#FFFFFF',
          100: '#FCFBF7',
          200: '#F8F7F0',
          300: '#EFECE0',
          400: '#E4E0D0',
        },
        mutedLime: {
          50: '#FAFCF5',
          100: '#F2F8E7',
          DEFAULT: '#D7E7B5', // Accent
          300: '#D7E7B5',
          400: '#C5DC9B',
          500: '#AFCB7E',
        },
        charcoal: {
          DEFAULT: '#202B24', // Text
          50: '#F6F7F6',
          100: '#E3E6E4',
          200: '#C2C8C4',
          300: '#99A39C',
          400: '#6B786F',
          500: '#48554C',
          600: '#343E37',
          700: '#202B24',
          800: '#161E19',
          900: '#0E1310',
        }
      },
      fontFamily: {
        display: ['"Playfair Display"', 'Georgia', 'serif'],
        sans: ['"Plus Jakarta Sans"', 'Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0 4px 20px -2px rgba(23, 77, 56, 0.06), 0 2px 6px -1px rgba(23, 77, 56, 0.04)',
        'card': '0 10px 30px -4px rgba(23, 77, 56, 0.08), 0 4px 10px -2px rgba(23, 77, 56, 0.03)',
        'elevated': '0 20px 40px -6px rgba(23, 77, 56, 0.12), 0 8px 16px -4px rgba(23, 77, 56, 0.06)',
      }
    },
  },
  plugins: [],
}
